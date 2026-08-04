from pathlib import Path
import json, sqlite3, sys

ROOT = Path(__file__).resolve().parents[1]
errors=[]

for path in (ROOT/'data/seeds').glob('*.json'):
    try:
        json.loads(path.read_text(encoding='utf-8'))
    except Exception as e:
        errors.append(f'{path.name}: invalid JSON: {e}')

standards=json.loads((ROOT/'data/seeds/standards.json').read_text(encoding='utf-8'))
ids={x['id'] for x in standards}
sources={x['sourceId'] for x in json.loads((ROOT/'data/seeds/sources.json').read_text(encoding='utf-8'))}
for x in standards:
    if x['sourceId'] not in sources: errors.append(f"standard {x['id']} missing source")
    if x.get('effectiveTo') and x.get('effectiveFrom') and x['effectiveTo'] < x['effectiveFrom']:
        errors.append(f"standard {x['id']} invalid dates")

bearings=json.loads((ROOT/'data/seeds/bearings.json').read_text(encoding='utf-8'))
for b in bearings:
    d=b['dimensionsMm']
    if not (0 < d['d'] < d['D'] and d['B']>0): errors.append(f"bearing {b['id']} invalid dimensions")

formulas=json.loads((ROOT/'data/seeds/formulas.json').read_text(encoding='utf-8'))
formula_ids=[f['id'] for f in formulas]
if len(formula_ids)!=len(set(formula_ids)): errors.append('duplicate formula id')

con=sqlite3.connect(ROOT/'database/reference_seed.sqlite')
try:
    result=con.execute('PRAGMA integrity_check').fetchone()[0]
    if result!='ok': errors.append('sqlite integrity: '+result)
    if con.execute('select count(*) from standards').fetchone()[0] != len(standards): errors.append('sqlite standards count mismatch')
finally: con.close()

if errors:
    print('\n'.join('ERROR: '+e for e in errors))
    sys.exit(1)
print(f'OK: {len(standards)} standards, {len(bearings)} bearings, {len(formulas)} formulas')

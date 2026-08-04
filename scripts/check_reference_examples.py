import math

def close(a,b,rel=1e-9):
    assert abs(a-b) <= rel*max(1,abs(b)), (a,b)

# Bearing life example: C/P=2, ball bearing p=3 -> 8 million revolutions
L10=(20000/10000)**3
close(L10,8.0)
L10h=1e6*L10/(60*1000)
close(L10h,133.33333333333334)

# Solid shaft torsion: formula self-consistency
T=1000.0; d=0.05
J=math.pi*d**4/32
tau=T*(d/2)/J
close(tau,16*T/(math.pi*d**3))

# Simply supported center load
P=1000; L=2; E=210e9; I=1e-6
M=P*L/4
f=P*L**3/(48*E*I)
assert M==500
assert f>0
print('OK: numerical reference examples')

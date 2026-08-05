package ru.constructor.handbook.projects.model

public data class ProjectBackup(
    val schemaVersion: Int,
    val exportedAt: String,
    val detail: ProjectDetail,
)

public object ProjectBackupJson {
    public fun export(backup: ProjectBackup): String = buildString {
        append('{')
        field("schemaVersion", backup.schemaVersion.toString(), quoted = false); comma()
        field("exportedAt", backup.exportedAt); comma()
        append("\"project\":{")
        with(backup.detail.project) {
            field("id", id); comma(); field("name", name); comma(); field("description", description.orEmpty()); comma(); field("createdAt", createdAt); comma(); field("updatedAt", updatedAt); comma(); field("archived", archived.toString(), quoted = false)
        }
        append("},\"counts\":{")
        field("assemblies", backup.detail.assemblies.size.toString(), quoted = false); comma()
        field("parts", backup.detail.parts.size.toString(), quoted = false); comma()
        field("measurements", backup.detail.measurements.size.toString(), quoted = false); comma()
        field("attachments", backup.detail.attachments.size.toString(), quoted = false); comma()
        field("notes", backup.detail.notes.size.toString(), quoted = false)
        append("}}")
    }

    public fun dryRunRestore(json: String): ProjectRestorePlan {
        val schema = Regex("\\\"schemaVersion\\\":(\\d+)").find(json)?.groupValues?.get(1)?.toIntOrNull()
        val projectId = Regex("\\\"id\\\":\\\"([^\\\"]+)\\\"").find(json)?.groupValues?.get(1)
        val projectName = Regex("\\\"name\\\":\\\"([^\\\"]+)\\\"").find(json)?.groupValues?.get(1)
        val issues = buildList {
            if (schema == null) add("schemaVersion отсутствует")
            if (schema != null && schema > 1) add("schemaVersion $schema новее поддерживаемой версии 1")
            if (projectId == null) add("project.id отсутствует")
        }
        return ProjectRestorePlan(schemaVersion = schema, projectId = projectId, projectName = projectName, canRestore = issues.isEmpty(), issues = issues)
    }

    private fun StringBuilder.field(name: String, value: String, quoted: Boolean = true) {
        append("\"$name\":")
        if (quoted) append("\"${value.replace("\\", "\\\\").replace("\"", "\\\"")}\"") else append(value)
    }
    private fun StringBuilder.comma() { append(',') }
}

public data class ProjectRestorePlan(
    val schemaVersion: Int?,
    val projectId: String?,
    val projectName: String?,
    val canRestore: Boolean,
    val issues: List<String>,
)

public object PrivacySafePhotoHandler {
    public fun sanitize(uri: String, mimeType: String?, caption: String?): PrivacySafePhotoAttachment = PrivacySafePhotoAttachment(
        uri = uri.substringBefore('?').substringBefore('#'),
        mimeType = mimeType?.lowercase(),
        caption = caption?.take(240),
        metadataStripped = true,
        persistablePermissionRequired = uri.startsWith("content://"),
    )
}

public data class PrivacySafePhotoAttachment(
    val uri: String,
    val mimeType: String?,
    val caption: String?,
    val metadataStripped: Boolean,
    val persistablePermissionRequired: Boolean,
)

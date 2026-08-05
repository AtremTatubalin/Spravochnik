package ru.constructor.handbook.projects.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ProjectBackupJsonTest {
    @Test fun exportsDryRunsRestoreAndSanitizesPhotoUri() {
        val detail = ProjectDetail(
            project = Project("p1", "Проект", null, "now", "now", false),
            assemblies = emptyList(),
            parts = emptyList(),
            measurements = emptyList(),
            attachments = emptyList(),
            notes = emptyList(),
        )
        val json = ProjectBackupJson.export(ProjectBackup(1, "now", detail))
        val plan = ProjectBackupJson.dryRunRestore(json)
        val photo = PrivacySafePhotoHandler.sanitize("content://photos/1?token=secret", "IMAGE/JPEG", "caption")

        assertTrue(plan.canRestore)
        assertEquals("p1", plan.projectId)
        assertEquals("content://photos/1", photo.uri)
        assertEquals("image/jpeg", photo.mimeType)
        assertTrue(photo.metadataStripped)
        assertTrue(photo.persistablePermissionRequired)
        assertFalse(ProjectBackupJson.dryRunRestore("{}").canRestore)
    }
}

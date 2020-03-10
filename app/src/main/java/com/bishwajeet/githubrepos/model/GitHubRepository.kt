package com.bishwajeet.githubrepos.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "github_repository")
data class GitHubRepository(
    @PrimaryKey @field:SerializedName("id") val id: Long,
    @field:SerializedName("name") val name: String,
    @field:SerializedName("full_name") val fullName: String,
    @field:SerializedName("description") val description: String?,
    @field:SerializedName("html_url") val url: String,
    @field:SerializedName("language") val language: String?
) : Serializable {
    override fun toString(): String {
        return "GitHubRepository(id=$id, name='$name', fullName='$fullName', description=$description, url='$url', language=$language)"
    }
}
package com.evaluation.githubrepository.data.remote.dto.github.repo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Organization(
    @SerialName("avatar_url")
    val avatarUrl: String? = null,
    @SerialName("events_url")
    val eventsUrl: String? = null,
    @SerialName("followers_url")
    val followersUrl: String? = null,
    @SerialName("following_url")
    val followingUrl: String? = null,
    @SerialName("gists_url")
    val gistsUrl: String? = null,
    @SerialName("gravatar_id")
    val gravatarId: String? = null,
    @SerialName("html_url")
    val htmlUrl: String? = null,
    val id: Int? = null,
    val login: String? = null,
    @SerialName("node_id")
    val nodeId: String? = null,
    @SerialName("organizations_url")
    val organizationsUrl: String? = null,
    @SerialName("received_events_url")
    val receivedEventsUrl: String? = null,
    @SerialName("repos_url")
    val reposUrl: String? = null,
    @SerialName("site_admin")
    val siteAdmin: Boolean? = null,
    @SerialName("starred_url")
    val starredUrl: String? = null,
    @SerialName("subscriptions_url")
    val subscriptionsUrl: String? = null,
    val type: String? = null,
    val url: String? = null,
)

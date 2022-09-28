package com.megWorld.universal.entities.productSubordinates

data class Links(
    val about: List<About>,
    val collection: List<Collection>,
    val curies: List<Cury>,
    val replies: List<Reply>,
    val self: List<Self>,
    val attachment: List<WpAttachment>,
    val featuredmedia: List<WpFeaturedmedia>,
    val term: List<WpTerm>
)
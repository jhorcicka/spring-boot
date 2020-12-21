package nl.kuba.demo.entities

import nl.kuba.demo.toSlug
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne

@Entity
class Article {
    var title: String = ""
    var headline: String = ""
    var content: String = ""
    @ManyToOne
    lateinit var author: User
    var slug: String = title.toSlug()
    var addedAt: LocalDateTime = LocalDateTime.now()
    @Id @GeneratedValue var id: Long? = null

    constructor() {
    }

    constructor(title: String, headline: String, content: String, author: User, slug: String = title.toSlug(),
                addedAt: LocalDateTime = LocalDateTime.now()) {
        this.title = title
        this.headline = headline
        this.content = content
        this.author = author
        this.slug = slug
        this.addedAt = addedAt
    }
}

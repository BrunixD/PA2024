package main
/**
 * Represents a directory entity within a directory structure.
 * This class implements the [Entity] interface.
 *
 * @property name The name of the directory entity.
 * @property parent The parent directory entity of the directory entity, if any.
 * @property attributes Additional attributes associated with the directory entity, stored as key-value pairs.
 */
data class DirectoryEntity(
    override var name: String,
    override val parent: DirectoryEntity? = null,
    override val attributes: MutableMap<String, String>? = mutableMapOf()
) : Entity {

    /**
     * The list of child entities contained within this directory entity.
     */
    val children: MutableList<Entity> = mutableListOf()

    init {
        parent?.children?.add(this)
    }

    /**
     * Accepts a visitor function and applies it recursively to each child entity.
     *
     * @param visitor A function that accepts an [Entity] and returns a Boolean indicating whether to continue visiting its children.
     */
    override fun accept(visitor: (Entity) -> Boolean) {
        if (visitor(this)) {
            children.forEach { it.accept(visitor) }
        }
    }

    /**
     * Generates a pretty-printed representation of the directory entity with optional indentation.
     *
     * @param indentLevel The level of indentation to apply.
     * @return A string containing the pretty-printed representation of the directory entity.
     */
    override fun prettyPrint(indentLevel: Int): String = buildString {
        val indentation = "\t".repeat(indentLevel)
        val attributesStr = attributes?.entries?.joinToString(separator = " ") { (key, value) -> "$key=\"$value\"" }.orEmpty()
        val attributePrefix = if (attributesStr.isNotEmpty()) " " else ""
        val tagStart = "$indentation<$name$attributePrefix$attributesStr>"
        val tagEnd = "$indentation</$name>\n"

        append("$tagStart\n")

        children.forEach { child ->
            val childAttributesStr = child.attributes?.entries?.joinToString(separator = " ") { (key, value) -> "$key=\"$value\"" }.orEmpty()
            val childAttributePrefix = if (childAttributesStr.isNotEmpty()) " " else ""
            val content = (child as? NestedEntity)?.content.orEmpty()
            append(
                if (child is DirectoryEntity) {
                    if (child.children.isEmpty()) "$indentation\t<${child.name}$childAttributePrefix$childAttributesStr/>\n"
                    else child.prettyPrint(indentLevel + 1)
                } else {
                    if (content.isNotEmpty()) "$indentation\t<${child.name}$childAttributePrefix$childAttributesStr>$content</${child.name}>\n"
                    else "$indentation\t<${child.name}$childAttributePrefix$childAttributesStr/>\n"
                }
            )
        }

        append(tagEnd)
    }
}



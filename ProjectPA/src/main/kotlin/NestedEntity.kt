package main
/**
 * Represents a nested entity within a directory structure.
 * This class implements the [Entity] interface.
 *
 * @property name The name of the nested entity.
 * @property content The content associated with the nested entity.
 * @property parent The parent directory entity of the nested entity, if any.
 * @property attributes Additional attributes associated with the nested entity, stored as key-value pairs.
 */
data class NestedEntity(
    override var name: String,
    val content: String? = null,
    override val parent: DirectoryEntity? = null,
    override val attributes: MutableMap<String, String>? = mutableMapOf()
) : Entity {

    init {
        parent?.children?.add(this)
    }

    /**
     * Generates a pretty-printed representation of the nested entity with optional indentation.
     *
     * @param indentLevel The level of indentation to apply.
     * @return A string containing the pretty-printed representation of the nested entity.
     */
    override fun prettyPrint(indentLevel: Int): String = buildString {
        // Construct the string representation of attributes
        val attributesStr = attributes?.entries?.joinToString(separator = " ") { (key, value) -> "$key=\"$value\"" }.orEmpty()
        // Determine if attributes are present to decide the prefix
        val attributePrefix = if (attributesStr.isNotEmpty()) " " else ""
        // Construct the content tag
        val contentTag = content?.let { ">$it</$name>" } ?: "/>"
        // Append the final formatted string
        append("<$name$attributePrefix$attributesStr$contentTag\n")
    }
}

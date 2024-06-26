package main
/**
 * Represents a SelfClosingElement entity within a ParentElement structure.
 * This class implements the [Element] interface.
 *
 * @property name The name of the SelfClosingElement entity.
 * @property content The content associated with the SelfClosingElement entity.
 * @property parent The parent ParentElement entity of the SelfClosingElement entity, if any.
 * @property attributes Additional attributes associated with the SelfClosingElement entity, stored as key-value pairs.
 */
data class SelfClosingElement(
    override var name: String,
    var content: String? = null,
    override val parent: ParentElement? = null,
    override val attributes: MutableMap<String, String>? = mutableMapOf()
) : Element {

    init {
        parent?.children?.add(this)
    }

    /**
     * Generates a pretty-printed representation of the SelfClosingElement entity with optional indentation.
     *
     * @param indentLevel The level of indentation to apply.
     * @return A string containing the pretty-printed representation of the SelfClosingElement entity.
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

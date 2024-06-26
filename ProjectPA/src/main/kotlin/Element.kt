package main
/**
 * Represents an entity within a ParentElement structure.
 * This interface is sealed, meaning all implementations must be declared in the same file.
 */
sealed interface Element {

    /**
     * The name of the entity.
     */
    var name: String

    /**
     * The parent ParentElement entity of the entity, if any.
     */
    val parent: ParentElement?

    /**
     * Additional attributes associated with the entity, stored as key-value pairs.
     */
    val attributes: MutableMap<String, String>?

    /**
     * Accepts a visitor function and applies it to the current entity.
     * By default, this function simply invokes the visitor function on the current entity.
     *
     * @param visitor A function that accepts an [Element] and returns a Boolean indicating whether to continue visiting its children.
     */
    fun accept(visitor: (Element) -> Boolean) {
        visitor(this)
    }

    /**
     * Generates a pretty-printed representation of the entity with optional indentation.
     * This function must be implemented by concrete subclasses.
     *
     * @param indentLevel The level of indentation to apply.
     * @return A string containing the pretty-printed representation of the entity.
     */
    fun prettyPrint(indentLevel: Int = 0): String

}

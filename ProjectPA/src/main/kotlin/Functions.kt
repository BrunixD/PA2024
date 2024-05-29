package main

/**
 * Contains extension functions related to Elements.
 */


/**
 * Removes the entity from its parent's list of children.
 */
fun Element.removeEntity() {
    this.getParent()?.getChildren()!!.remove(this)
}

/**
 * Adds a new ParentElement entity to the current ParentElement entity.
 *
 * @param entityName The name of the ParentElement entity to add.
 * @param entityAttributes Optional attributes associated with the ParentElement entity.
 */
fun ParentElement.addParentElementEntity(entityName: String, entityAttributes: MutableMap<String, String>? = null) {
    ParentElement(name = entityName, parent = this, attributes = entityAttributes)
}

/**
 * Adds a new SelfClosingElement entity to the current ParentElement entity.
 *
 * @param entityName The name of the SelfClosingElement entity to add.
 * @param entityContent Optional content associated with the SelfClosingElement entity.
 * @param entityAttributes Optional attributes associated with the SelfClosingElement entity.
 */
fun ParentElement.addSelfClosingElementEntity(entityName: String, entityContent: String? = null, entityAttributes: MutableMap<String, String>? = null) {
    SelfClosingElement(name = entityName, parent = this, content = entityContent, attributes = entityAttributes)
}

/**
 * Adds a new attribute to the entity.
 * Returns exception if attribute key already exists
 *
 * @param attributeKey The key of the attribute to add.
 * @param attributeValue The value of the attribute to add.
 */
fun Element.addAttribute(attributeKey: String, attributeValue: String) {
    this.attributes?.keys?.forEach { key->
        if (key == attributeKey) throw IllegalArgumentException("Attribute already exists")
    }
    this.attributes?.put(attributeKey, attributeValue)
}

/**
 * Removes an attribute from the entity.
 *
 * @param attributeKey The key of the attribute to remove.
 */
fun Element.removeAttribute(attributeKey: String) {
    this.attributes!!.remove(attributeKey)
}

/**
 * Modifies the value of an existing attribute in the entity.
 *
 * @param attributeKey The key of the attribute to modify.
 * @param attributeValue The new value for the attribute.
 */
fun Element.alterAttribute(attributeKey: String, attributeValue: String) {
    this.attributes!!.replace(attributeKey, attributeValue)
}

/**
 * Retrieves the parent ParentElement entity of the entity.
 *
 * @return The parent ParentElement entity, or null if the entity has no parent.
 */
fun Element.getParent(): ParentElement? {
    return if (this.parent != null)
        this.parent
    else
        null
}

/**
 * Retrieves the list of child entities of the ParentElement entity.
 *
 * @return The list of child entities.
 */
fun ParentElement.getChildren(): MutableList<Element> {
    return this.children
}

/**
 * Adds an attribute to all entities with the specified name within the ParentElement entity's hierarchy.
 *
 * @param entityName The name of the entities to which the attribute will be added.
 * @param nome The name of the attribute to add.
 * @param value The value of the attribute to add.
 */
fun ParentElement.addAttributeGlobal(entityName: String, nome: String, value: String) {
    this.accept {
        if (it.name == entityName) it.addAttribute(nome, value)
        true
    }
}

/**
 * Renames all entities with the specified old name to the new name within the ParentElement entity's hierarchy.
 *
 * @param entityOld The old name of the entities to be renamed.
 * @param entityNew The new name for the entities.
 */
fun ParentElement.renameEntityGlobal(entityOld: String, entityNew: String) {
    this.accept {
        if (it.name == entityOld) {
            it.name = entityNew
        }
        true
    }
}

/**
 * Renames an attribute across all entities with the specified name within the ParentElement entity's hierarchy.
 *
 * @param entityName The name of the entities to search for.
 * @param attributeNameOld The old name of the attribute to rename.
 * @param attributeNameNew The new name for the attribute.
 */
fun ParentElement.renameAttributeNameGlobal(entityName: String, attributeNameOld: String, attributeNameNew: String) {
    this.accept {
        if (it.name == entityName) {
            val savedValue = it.attributes?.get(attributeNameOld)!!
            it.attributes?.put(attributeNameNew, savedValue)
            it.attributes?.remove(attributeNameOld)
        }
        true
    }
}

/**
 * Removes all entities with the specified name from the ParentElement entity's hierarchy.
 *
 * @param entityName The name of the entities to remove.
 */
fun Element.removeEntityGlobal(entityName: String) {
    val entitiesToDelete: MutableList<Element> = mutableListOf()
    this.accept {
        if (it.name == entityName) entitiesToDelete.add(it)
        true
    }
    entitiesToDelete.forEach { entity -> entity.removeEntity() }
}

/**
 * Removes an attribute from all entities with the specified name within the ParentElement entity's hierarchy.
 *
 * @param entityName The name of the entities to search for.
 * @param attributeKeyName The key of the attribute to remove.
 */
fun Element.removeAttributeGlobal(entityName: String, attributeKeyName: String) {
    this.accept {
        if (it.name == entityName) it.removeAttribute(attributeKeyName)
        true
    }
}

/**
 * Performs an XPath-like search across the ParentElement entity's hierarchy and returns matching entities as a string.
 *
 * @param path The XPath-like path to search for.
 * @return A string containing the pretty-printed representation of the matching entities.
 */
fun Element.xPath(path: String): String {
    val parts = path.split("/").toMutableList()
    val result = StringBuilder()

    this.accept { entity ->
        if (entity.name == parts.first()) {
            if (parts.size == 1) result.append(entity.prettyPrint())
            else parts.removeAt(0)
            true
        } else {
            false
        }
    }

    return result.toString()
}

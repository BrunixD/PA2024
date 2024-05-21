package main

/**
 * Contains extension functions related to entities and directory entities.
 */


    /**
     * Removes the entity from its parent's list of children.
     */
    fun Entity.removeEntity() {
        this.getParent()?.getChildren()!!.remove(this)
    }

    /**
     * Adds a new directory entity to the current directory entity.
     *
     * @param entityName The name of the directory entity to add.
     * @param entityAttributes Optional attributes associated with the directory entity.
     */
    fun DirectoryEntity.addDirectoryEntity(entityName: String, entityAttributes: MutableMap<String, String>? = null) {
        DirectoryEntity(name = entityName, parent = this, attributes = entityAttributes)
    }

    /**
     * Adds a new nested entity to the current directory entity.
     *
     * @param entityName The name of the nested entity to add.
     * @param entityContent Optional content associated with the nested entity.
     * @param entityAttributes Optional attributes associated with the nested entity.
     */
    fun DirectoryEntity.addNestedEntity(entityName: String, entityContent: String? = null, entityAttributes: MutableMap<String, String>? = null) {
        NestedEntity(name = entityName, parent = this, content = entityContent, attributes = entityAttributes)
    }

    /**
     * Adds a new attribute to the entity.
     *
     * @param attributeKey The key of the attribute to add.
     * @param attributeValue The value of the attribute to add.
     */
    fun Entity.addAttribute(attributeKey: String, attributeValue: String) {
        this.attributes?.put(attributeKey, attributeValue)
    }

    /**
     * Removes an attribute from the entity.
     *
     * @param attributeKey The key of the attribute to remove.
     */
    fun Entity.removeAttribute(attributeKey: String) {
        this.attributes!!.remove(attributeKey)
    }

    /**
     * Modifies the value of an existing attribute in the entity.
     *
     * @param attributeKey The key of the attribute to modify.
     * @param attributeValue The new value for the attribute.
     */
    fun Entity.alterAttribute(attributeKey: String, attributeValue: String) {
        this.attributes!!.replace(attributeKey, attributeValue)
    }

    /**
     * Retrieves the parent directory entity of the entity.
     *
     * @return The parent directory entity, or null if the entity has no parent.
     */
    fun Entity.getParent(): DirectoryEntity? {
        return this.parent
    }

    /**
     * Retrieves the list of child entities of the directory entity.
     *
     * @return The list of child entities.
     */
    fun DirectoryEntity.getChildren(): MutableList<Entity> {
        return this.children
    }

    /**
     * Adds an attribute to all entities with the specified name within the directory entity's hierarchy.
     *
     * @param entityName The name of the entities to which the attribute will be added.
     * @param nome The name of the attribute to add.
     * @param value The value of the attribute to add.
     */
    fun DirectoryEntity.addAttributeGlobal(entityName: String, nome: String, value: String) {
        this.accept {
            if (it.name == entityName) it.addAttribute(nome, value)
            true
        }
    }

/**
 * Renames all entities with the specified old name to the new name within the directory entity's hierarchy.
 *
 * @param entityOld The old name of the entities to be renamed.
 * @param entityNew The new name for the entities.
 */
fun DirectoryEntity.renameEntityGlobal(entityOld: String, entityNew: String) {
    this.accept {
        if (it.name == entityOld) {
            it.name = entityNew
        }
        true
    }
}

/**
 * Renames an attribute across all entities with the specified name within the directory entity's hierarchy.
 *
 * @param entityName The name of the entities to search for.
 * @param attributeNameOld The old name of the attribute to rename.
 * @param attributeNameNew The new name for the attribute.
 */
fun DirectoryEntity.renameAttributeNameGlobal(entityName: String, attributeNameOld: String, attributeNameNew: String) {
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
 * Removes all entities with the specified name from the directory entity's hierarchy.
 *
 * @param entityName The name of the entities to remove.
 */
fun Entity.removeEntityGlobal(entityName: String) {
    val entitiesToDelete: MutableList<Entity> = mutableListOf()
    this.accept {
        if (it.name == entityName) entitiesToDelete.add(it)
        true
    }
    entitiesToDelete.forEach { entity -> entity.removeEntity() }
}

/**
 * Removes an attribute from all entities with the specified name within the directory entity's hierarchy.
 *
 * @param entityName The name of the entities to search for.
 * @param attributeKeyName The key of the attribute to remove.
 */
fun Entity.removeAttributeGlobal(entityName: String, attributeKeyName: String) {
    this.accept {
        if (it.name == entityName) it.removeAttribute(attributeKeyName)
        true
    }
}

/**
 * Performs an XPath-like search across the directory entity's hierarchy and returns matching entities as a string.
 *
 * @param path The XPath-like path to search for.
 * @return A string containing the pretty-printed representation of the matching entities.
 */
fun Entity.xPath(path: String): String {
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

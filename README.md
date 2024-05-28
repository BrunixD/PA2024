# PA2024
Directory Structure Management

This project provides classes and utilities to manage directory structures in Kotlin. It includes functionalities to represent entities within a directory, add, remove, and manipulate attributes and nested entities, as well as pretty-print directory structures.
Features

    Entity Representation: Classes ParentElement and SelfClosingElement represent directory entities and nested entities, respectively.
    Attribute Management: Add, remove, and alter attributes associated with entities.
    Nested Entity Addition and Removal: Add nested entities to directory entities and remove entities from their parent's list of children.
    Pretty-Printing: Generate a readable representation of directory structures with indentation.
    Global Operations: Perform operations (addition, removal, renaming) across all entities with specific names or attributes within the directory hierarchy.
    XPath-Like Search: Perform search operations across the directory structure using an XPath-like syntax.

Usage

    Class Definitions: Define directory entities (ParentElement) and nested entities (SelfClosingElement) with appropriate properties and attributes.

    Functionality Usage: Use provided functions like addAttribute, removeAttribute, addNestedEntity, etc., to manipulate entities and attributes as needed.

    Pretty-Printing: Utilize the prettyPrint function to generate a readable representation of the directory structure.

    Global Operations: Use functions like addAttributeGlobal, renameEntityGlobal, removeEntityGlobal, etc., to perform operations across the entire directory hierarchy.

    Testing: Use provided unit tests to verify the correctness of functionalities.

Testing

The project includes comprehensive unit tests to ensure the correctness of each functionality. Tests cover entity addition, attribute management, pretty-printing, global operations, and more.

To run the tests, execute the tests class.
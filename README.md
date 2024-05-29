# XML Processing Library

A kotlin library for processing and manipulating XML Documents.
Developed as final project for the course [Advanced Programming](https://fenix-mais.iscte-iul.pt/courses/m4310-284502928661869/fuc) , under the teaching of Professor _André Santos_, int Master's in Computer Engineering at ISCTE - University Institute of Lisbon.


# Contents


## Part 1: Model

- Add and remove entities;
- Add, remove and alter entities' attributes
- Access the parent entity and child entities of an entity
- Pretty print as a string
- Traverse the document with visitor objects (Visitor)
- Add attributes globally to the document (providing entity name, attribute name, and value)
- Rename entities globally in the document (providing old name and new name)
- Rename attributes globally in the document (providing entity name, old attribute name, and new attribute name)
- Remove entities globally from the document (providing name)
- Remove attributes globally from the document (providing entity name and attribute name)
- MicroXPath
- Validations

## Part 2: Mapping XML Classes

- Translate an object to XML
- Define annotations for translation
- Determine how attributes, entities and content are translated
- Change order of the XML entity depending on a specified given class

# Instructions

## Part 1 :

In this library you can create and manipulate the following entities :
- Data Classes: ParentElement and SelfClosingElement
- Interface: Element

A Document needs to always have a root (ParentElement) with its version and encoding as attributes, and its name. From the root it can be created furthermore ParentElements or SelfClosingElements.

## Examples

- Creating the Document

  val attributes = mutableMapOf<String,String>()
  attributes.put("version","1.0")
  attributes.put("encoding","UTF-8")
  val rootElement = ParentElement(name="root", parent=null, attributes=attributes)

An Element is an Interface, in which each Element can be a ParentElement or SelfClosingElement depending on its characteristics. Both Elements need to have a name and may or may not have attributes imbued in them.
A ParentElement is an entity that may or may not have have a parent but has children to it.
A SelfClosingElement is an entity that may or may not have a parent, doesn't have children and may or may not have content /text in between it.

- Creating a ParentElement and a SelfClosingElement

  val parentElement = ParentElement(name="name", parent="null", attributes="null")
  val selfClosingElement = SelfClosingElement(name="name", parent=parentElement, attributes=null, content="I'm a text between my entitiy")

## Table of functions

The following contains the main functions of this library.

| Function Name                         | Description                                                                                    | Arguments                                        |
|------------------------------------------|-------------------------------------------------------------------------------------------------|--------------------------------------------------|
| `removeEntity`                             | Removes the entity from its parent's list of children.                                                  | None                    |
| `addParentElementEntity`                              | Adds a new `ParentElement` entity to the current `ParentElement` entity.                                            | `entityName: String`, `entityAttributes: MutableMap<String, String>? = null`                                   |
| `addSelfClosingElementEntity`                         | Adds a new `SelfClosingElement` entity to the current `ParentElement` entity..                | `entityName: String`, `entityContent: String? = null`, `entityAttributes: MutableMap<String, String>? = null`                                             |
| `addAttribute`              | Adds a new attribute to the entity. Throws an exception if the attribute key already exists.                                  | `attributeKey: String`, `attributeValue: String`                                             |
| `removeAttribute`                          | Removes an attribute from the entity.                             | `attributeKey: String`                                   |
| `alterAttribute`            | Modifies the value of an existing attribute in the entity.   | `attributeKey: String`, `attributeValue: String`                                   |
| `getParent`                     | Retrieves the parent `ParentElement` entity of the entity.         | None                                   |
| `getChildren`                          | Retrieves the list of child entities of the `ParentElement` entity.                             | None                                   |
| `addAttributeGlobal`            | Adds an attribute to all entities with the specified name within the `ParentElement` hierarchy.   | `entityName: String`, `nome: String`, `value: String`                                   |
| `renameEntityGlobal`                     | Renames all entities with the specified old name to the new name within the `ParentElement` hierarchy.        | `entityOld: String`, `entityNew: String`                                   |
| `renameAttributeNameGlobal`                          | Renames an attribute across all entities with the specified name within the `ParentElement` hierarchy.                            | `entityName: String`, `attributeNameOld: String`, `attributeNameNew: String`                                  |
| `removeEntityGlobal`            | Removes all entities with the specified name from the `ParentElement` hierarchy.   | `entityName: String`                                   |
| `removeAttributeGlobal`                     | Removes an attribute from all entities with the specified name within the `ParentElement` hierarchy.         | `entityName: String`, `attributeKeyName: String`                                   |
| `xPath`                     | Performs an XPath-like search across the `ParentElement` hierarchy and returns matching entities as a string.         | `path: String`                                   |

## Part 2 :

The purpose of this part is to obtain XML entities automatically through a translation of objects based on the structure of its classes, giving some flexibility and customization with custom annotations in the classes.
The following are the custom annotations created.

| Annotation Name                          | Description                                                                                     | Arguments                                        |
|------------------------------------------|-------------------------------------------------------------------------------------------------|--------------------------------------------------|
| `XmlAdapter`                             | Specifies an adapter class for an XML element.                                                  | `adapter: KClass<out Adapter>`                   |
| `Attribute`                              | Marks a property as an attribute in an XML element.                                             | `name: String`                                   |
| `WithPercentage`                         | Indicates that a property's value should be suffixed with a percentage sign (%).                | None                                             |
| `SelfClosingElementContent`              | Marks a property as the content of a self-closing XML element.                                   | None                                             |
| `ParentElement`                          | Marks a class or property as a parent element in the XML structure.                             | `name: String`                                   |
| `SelfClosingElementPrimitive`            | Marks a property (primitive string) as a self-closing element attribute in the XML structure.   | `name: String`                                   |
| `SelfClosingElement`                     | Marks a property (class type) as a self-closing element attribute in the XML structure.         | `name: String`                                   |

With this annotations, the translation of the object to an XML entity is much more complete due to its specific search for them.
For example, if you specify a property as an Attribute using its annotation, then the XML entity created will have that property as an attribute.

## Example

- A class with annotations :



@Anotations.ParentElement("Fuc")  
class FUC(  
@Anotations.Attribute("Código")  
val codigo: String,  
@Anotations.SelfClosingElementPrimitive("Nome")  
val nome: String,  
@Anotations.SelfClosingElementPrimitive("Ects")  
val ects: Double,  
@Anotations.SelfClosingElementPrimitive("Observações")  
val observacoes: String,  
@Anotations.SelfClosingElement("NN")  
val ex: ExampleSelfClosingElementAsClass,
    )

- XML creation from an object from previous class


  code:

  `val ex = ExampleSelfClosingElementAsClass("ola") ` <br>
  `val f = FUC("M4310", "Programação Avançada", 6.0, "la la...", ex)`


  output:


    <Fuc Código="M4310">
      <ects>6.0</ects>
      <Example Atributo de example="ola"/>
      <nome>Programação Avançada</nome>
      <observacoes>la la...</observacoes>
    </Fuc>


# Credits
Credit for all the code in this repository goes to [Bruno Carvalho](https://github.com/BrunixD) and [Miguel Cordeiro](https://github.com/mrsco2-iscte).


# XML Processing Library (Kotlin)

A Kotlin library for building, manipulating, and querying XML documents — plus an annotation-based mapper that turns Kotlin objects into XML automatically via reflection.

Developed as the final project for [Advanced Programming](https://fenix-mais.iscte-iul.pt/courses/m4310-284502928661869/fuc) in the MSc in Computer Engineering at ISCTE — University Institute of Lisbon, taught by Professor André Santos.

## Highlights

- **Document model** with a clean `Element` interface and two implementations: `ParentElement` (has children) and `SelfClosingElement` (leaf, optional text content)
- **Visitor pattern** for traversing documents
- **Global operations** — add/rename/remove entities and attributes across an entire document tree in one call
- **MicroXPath** — a small XPath-like query language for finding entities by path
- **Annotation-based object -> XML mapping** — mark up your classes with annotations and translate instances to XML automatically, including custom adapters for full control over the output
- **Validations** and pretty-printing to string

## Part 1 — Document model

Every document has a root `ParentElement` carrying the XML version and encoding as attributes. From the root you compose the tree out of further `ParentElement`s and `SelfClosingElement`s.

```kotlin
// Create a document root
val rootElement = ParentElement(
    name = "root",
    parent = null,
    attributes = mutableMapOf("version" to "1.0", "encoding" to "UTF-8")
)

// Add children
val parent = ParentElement(name = "books", parent = rootElement, attributes = null)
val leaf = SelfClosingElement(
    name = "title",
    parent = parent,
    attributes = null,
    content = "Text content between the tags"
)
```

### Main API

| Function | Description | Arguments |
|---|---|---|
| `removeEntity` | Removes the entity from its parent's list of children | — |
| `addParentElementEntity` | Adds a new `ParentElement` child to this entity | `entityName`, `entityAttributes?` |
| `addSelfClosingElementEntity` | Adds a new `SelfClosingElement` child to this entity | `entityName`, `entityContent?`, `entityAttributes?` |
| `addAttribute` | Adds an attribute; throws if the key already exists | `attributeKey`, `attributeValue` |
| `removeAttribute` | Removes an attribute | `attributeKey` |
| `alterAttribute` | Changes an existing attribute's value | `attributeKey`, `attributeValue` |
| `getParent` / `getChildren` | Navigate the tree | — |
| `addAttributeGlobal` | Adds an attribute to all entities with a given name, document-wide | `entityName`, `name`, `value` |
| `renameEntityGlobal` | Renames all entities with a given name, document-wide | `entityOld`, `entityNew` |
| `renameAttributeNameGlobal` | Renames an attribute on all entities with a given name | `entityName`, `attributeNameOld`, `attributeNameNew` |
| `removeEntityGlobal` | Removes all entities with a given name, document-wide | `entityName` |
| `removeAttributeGlobal` | Removes an attribute from all entities with a given name | `entityName`, `attributeKeyName` |
| `xPath` | XPath-like search over the tree; returns matching entities | `path` |

## Part 2 — Mapping objects to XML

Annotate your classes to control how instances translate into XML — which properties become attributes, which become child elements, element naming, ordering, and custom adapters for anything else.

### Annotations

| Annotation | Description | Arguments |
|---|---|---|
| `ParentElement` | Marks a class or property as a parent element | `name` |
| `Attribute` | Maps a property to an XML attribute | `name` |
| `SelfClosingElementPrimitive` | Maps a primitive property to a self-closing element | `name` |
| `SelfClosingElement` | Maps a class-typed property to a self-closing element | `name` |
| `SelfClosingElementContent` | Marks a property as a self-closing element's text content | — |
| `WithPercentage` | Suffixes the property's value with `%` | — |
| `XmlAdapter` | Plugs in a custom adapter class for full control of the output | `adapter: KClass<out Adapter>` |

### Example

The domain here is a university course unit description (FUC — *Ficha de Unidade Curricular*), the running example from the course:

```kotlin
@Annotations.ParentElement("Fuc")
class FUC(
    @Annotations.Attribute("Código")
    val codigo: String,
    @Annotations.SelfClosingElementPrimitive("Nome")
    val nome: String,
    @Annotations.SelfClosingElementPrimitive("Ects")
    val ects: Double,
    @Annotations.SelfClosingElementPrimitive("Observações")
    val observacoes: String,
    @Annotations.SelfClosingElement("NN")
    val ex: ExampleSelfClosingElementAsClass,
)

val ex = ExampleSelfClosingElementAsClass("ola")
val f = FUC("M4310", "Programação Avançada", 6.0, "la la...", ex)
```

Output:

```xml
<Fuc Código="M4310">
  <ects>6.0</ects>
  <Example Atributo de example="ola"/>
  <nome>Programação Avançada</nome>
  <observacoes>la la...</observacoes>
</Fuc>
```

## Authors

[Bruno Carvalho](https://github.com/BrunixD) and [Miguel Cordeiro](https://github.com/mrsco2-iscte)

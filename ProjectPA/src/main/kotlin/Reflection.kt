package main.kotlin

import main.ParentElement
import main.Element
import main.SelfClosingElement
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation

// Define the Adapter interface
interface Adapter {
    /**
     * Adapts the given element based on a specified order map.
     *
     * @param element The element to adapt.
     * @param orderMap A map specifying the order of elements.
     * @return The adapted element.
     */
    fun adapt(element: Element, orderMap: Map<String, Int>?): Element
}

// Define a FUC class example annotated with ParentElement
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

// Define a ExampleSelfClosingElementAsClass class example annotated with SelfClosingElement
@Anotations.SelfClosingElement("Example")
class ExampleSelfClosingElementAsClass (
    @Anotations.Attribute("Atributo de example")
    val example : String,
)

// Define an example of SelfClosingElement class annotated with SelfClosingElement
@Anotations.SelfClosingElement("SelfClosing Element")
class NESTED(
    @Anotations.Attribute("Sou um Atributo")
    val atributo: String,
    @Anotations.SelfClosingElementContent()
    val conteudo: String,

    )

// Implement the FUCAdapter class
class FUCAdapter : Adapter {
    /**
     * Adapts the given element by sorting its children based on the provided order map.
     *
     * @param element The element to adapt.
     * @param orderMap A map specifying the order of elements.
     * @return The adapted element.
     */
    override fun adapt(element: Element, orderMap: Map<String, Int>?): Element {
        if (element is ParentElement && orderMap != null) {
            element.children.sortedBy { orderMap[it.name] ?: Int.MAX_VALUE }
            return element
        }
        return element
    }
}

/**
 * Translates an object into an Element structure.
 *
 * @param obj The object to translate.
 * @param lastParent The parent element, if any.
 * @param orderMap A map specifying the order of elements.
 * @return The translated element.
 */
fun translate(obj: Any, lastParent: ParentElement? = null, orderMap: Map<String, Int>? = null): Element {
    val isParentElement = obj::class.hasAnnotation<Anotations.ParentElement>()
    val name = obj::class.findAnnotation<Anotations.ParentElement>()?.name ?: obj::class.findAnnotation<Anotations.SelfClosingElement>()?.name
    val newEntity = if (isParentElement) ParentElement(name = name.toString(), lastParent) else SelfClosingElement(name = name.toString())

    val members = obj::class.declaredMemberProperties
    members.forEach { member ->
        val objValue = member.call(obj)
        if (objValue != null) {
            when {
                member.hasAnnotation<Anotations.Attribute>() -> {
                    val attributeName = member.findAnnotation<Anotations.Attribute>()?.name ?: member.name
                    val value =
                        member.call(obj).toString() + if (member.hasAnnotation<Anotations.WithPercentage>()) "%" else ""
                    newEntity.attributes?.put(attributeName, value)
                }

                member.hasAnnotation<Anotations.SelfClosingElementPrimitive>() -> {
                    val nestedName = member.name
                    val nestedContent = objValue.toString()
                    if (newEntity is ParentElement) SelfClosingElement(
                        nestedName,
                        nestedContent,
                        newEntity
                    ) else SelfClosingElement(nestedName, nestedContent)
                }

                member.hasAnnotation<Anotations.SelfClosingElement>() -> {
                    val nestedEntities = if (objValue is Collection<*>) objValue.map {
                        translate(
                            it!!,
                            newEntity as ParentElement,
                            orderMap
                        )
                    } else listOf(translate(objValue, newEntity as ParentElement, orderMap))
                    (newEntity as ParentElement).children.addAll(nestedEntities)
                }

                member.hasAnnotation<Anotations.ParentElement>() -> {
                    val directoryEntities = if (objValue is Collection<*>) objValue.map {
                        translate(
                            it!!,
                            newEntity as ParentElement,
                            orderMap
                        )
                    } else listOf(translate(objValue, newEntity as ParentElement, orderMap))
                    (newEntity as ParentElement).children.addAll(directoryEntities)
                }
            }
        }
    }

    // Apply the adapter if the XmlAdapter annotation is present
    if (obj::class.hasAnnotation<Anotations.XmlAdapter>()) {
        obj::class.findAnnotation<Anotations.XmlAdapter>()?.let { annotation ->
            val adapter = annotation.adapter.objectInstance ?: annotation.adapter.createInstance()
            return adapter.adapt(newEntity, orderMap)
        }
    }

    return newEntity
}

fun main(args: Array<String>) {
    val ex = ExampleSelfClosingElementAsClass("ola")
    val f = FUC("M4310", "Programação Avançada", 6.0, "la la...", ex)
    val n = NESTED("atributo123" , "vou ser um conteudo")
    val rootDirectory = translate(f)
    val rootDirectory2 = translate(n)
    println(rootDirectory.prettyPrint(0))
    println("\n" + rootDirectory2.prettyPrint(0))
}

package `Phase 2`

import main.DirectoryEntity
import main.Entity
import main.NestedEntity
import main.addAttribute
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation

class FUC(
        @Anotations.XmlAttribute("Código")
        val codigo: String,
        @Anotations.XmlElementName("Nome")
        val nome: String,
        @Anotations.XmlElementName("Ects")
        val ects: Double,
        @Anotations.XmlElementName("Observações")
        val observacoes: String,

        )

    fun translate(obj: Any) : Entity {
        val name = obj::class.simpleName
        val newObject = DirectoryEntity(name = name.toString());
        print(name)
        val members = obj::class.declaredMemberProperties
        members.forEach { member ->
            when {
                member.findAnnotation<Anotations.XmlAttribute>() != null -> {
                    newObject.attributes?.put(
                        member.findAnnotation<Anotations.XmlAttribute>()!!.name,
                        member.call(obj).toString()
                    )
                }

                member.findAnnotation<Anotations.XmlElementName>() != null -> {
                    val nestedName = member.findAnnotation<Anotations.XmlElementName>()!!.name
                    val nestedValue = member.call(obj).toString()
                    NestedEntity(nestedName, nestedValue, newObject)
                }
            }
        }
        return newObject
    }

    fun main(args: Array<String>) {
        val f = FUC("M4310", "Programação Avançada", 6.0, "la la...")
        val rootDirectory = translate(f)
        println(rootDirectory.prettyPrint(0))
    }


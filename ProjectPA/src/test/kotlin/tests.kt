package tests
import main.kotlin.Anotations
import main.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import main.kotlin.translate

val plano = ParentElement("plano")
val cursoPA = SelfClosingElement("curso", content = "Mestrado em Engenharia Informática", parent = plano)
val fucPA = ParentElement("fuc", parent = plano)
val nomePA = SelfClosingElement("nome", parent = fucPA, content = "Programação Avançada")
val ects = SelfClosingElement("ects", parent = fucPA, content = "6.0")
val avaliacao = ParentElement("avaliacao", parent = fucPA)
val componenteQ = SelfClosingElement("componente", parent = avaliacao, /*attributes = mutableMapOf("nome=\"Quizzes\"", "peso=\"20%\"")*/)
val componenteP = SelfClosingElement("componente", parent = avaliacao, /*attributes = mutableMapOf("nome=\"Projeto\"", "peso=\"80%\"")*/)
val fuc2 = ParentElement("fuc2", parent=plano, /*attributes =mutableMapOf("codigo='03782'")*/)

@Anotations.ParentElement("DWithN")
class ParentwithNest(
    @Anotations.Attribute("Código")
    val codigo: String,
    @Anotations.SelfClosingElementAttribute("Nome")
    val nome: String,
    @Anotations.SelfClosingElementAttribute("Ects")
    val ects: Double,
    @Anotations.SelfClosingElementAttribute("Observações")
    val observacoes: String,

    )
@Anotations.ParentElement("DWithoutN")
class ParentwithoutNest(
    @Anotations.Attribute("Código")
    val codigo: String,
)

@Anotations.SelfClosingElement("NestedWithContent")
class SelfClosingWithContent(
    @Anotations.Attribute("Sou um Atributo")
    val atributo: String,
    @Anotations.SelfClosingElementContent()
    val conteudo: String,

    )

@Anotations.SelfClosingElement("NestedWithoutContent")
class SelfClosingWithoutContent(
    @Anotations.Attribute("Sou um Atributo")
    val atributo: String,

    )

@Anotations.ParentElement("DWithListOfChildren")
class ParentwithListOfChildren(
    @Anotations.Attribute("Código")
    val codigo: String,
    @Anotations.SelfClosingElementAttribute("Nome")
    val nome: String,
    @Anotations.SelfClosingElementAttribute("Ects")
    val ects: Double,
    @Anotations.SelfClosingElementAttribute("Observações")
    val observacoes: String,
    @Anotations.SelfClosingElement("Avaliacao")
    val avaliacao: List<SelfClosingWithoutContent>
)

@Anotations.ParentElement("DWithListOfChildrenDirectory")
class ParentwithListOfChildrenDirectory(
    @Anotations.Attribute("Código")
    val codigo: String,
    @Anotations.SelfClosingElementAttribute("Nome")
    val nome: String,
    @Anotations.SelfClosingElementAttribute("Ects")
    val ects: Double,
    @Anotations.SelfClosingElementAttribute("Observações")
    val observacoes: String,
    @Anotations.ParentElement("Avaliacao")
    val avaliacao: List<ParentwithNest>
)

class tests{

    @Test
    fun removeEntityTest() {
        cursoPA.removeEntity()
        assertFalse(plano.children.contains(cursoPA))
    }

    /**
     * Tests the addition of a nested entity to a directory entity.
     */
    @Test
    fun addNestedEntityTest() {
        val testEntity = SelfClosingElement("testEntity")
        plano.addNestedEntity("testEntity")
        assertFalse(plano.children.contains(testEntity))
    }

    /**
     * Tests the addition of a directory entity to another directory entity.
     */
    @Test
    fun addDirectoryEntityTest() {
        val testEntity = SelfClosingElement("testEntity")
        plano.addDirectoryEntity("testEntity")
        assertFalse(plano.children.contains(testEntity))
    }

    /**
     * Tests the addition of an attribute to an entity.
     */
    @Test
    fun addAttributeTest() {
        val attributes: MutableMap<String, String> = mutableMapOf()
        attributes.put("content", "2020")
        val testEntity = ParentElement("testEntity")
        testEntity.addAttribute("content", "2020")
        assertEquals(attributes, testEntity.attributes)
    }

    /**
     * Tests the removal of an attribute from an entity.
     */
    @Test
    fun removeAttributeTest() {
        val attributes: MutableMap<String, String> = mutableMapOf()
        attributes.put("content", "2020")
        val testEntity = ParentElement("testEntity")
        testEntity.addAttribute("content", "2020")
        testEntity.removeAttribute("content")
        assertTrue(testEntity.attributes!!.isEmpty())
    }

    /**
     * Tests the alteration of an attribute value in an entity.
     */
    @Test
    fun alterAttributeTest() {
        val attributes: MutableMap<String, String> = mutableMapOf()
        attributes["content"] = "2020"
        val attributes2: MutableMap<String, String> = mutableMapOf()
        attributes2["content"] = "2000"
        val subCurso = ParentElement("aaa")
        subCurso.addAttribute("content", "2020")
        subCurso.alterAttribute("content", "2000")
        assertEquals(attributes2, subCurso.attributes)
    }

    /**
     * Tests the pretty-printing of a directory entity.
     */
    @Test
    fun prettyPrintTest() {
        componenteQ.addAttribute("nome", "Quizz")
        componenteQ.addAttribute("Peso", "20%")

        componenteP.addAttribute("nome", "Projeto")
        componenteP.addAttribute("Peso", "80%")

        cursoPA.addAttribute("content", "2020")
        plano.addNestedEntity(entityName = "teste", entityContent = "Isto e content")
        assertEquals(
            "<plano>\n" +
                    "\t<curso content=\"2020\">Mestrado em Engenharia Informática</curso>\n" +
                    "\t<fuc>\n" +
                    "\t\t<nome>Programação Avançada</nome>\n" +
                    "\t\t<ects>6.0</ects>\n" +
                    "\t\t<avaliacao>\n" +
                    "\t\t\t<componente nome=\"Quizz\" Peso=\"20%\"/>\n" +
                    "\t\t\t<componente nome=\"Projeto\" Peso=\"80%\"/>\n" +
                    "\t\t</avaliacao>\n" +
                    "\t</fuc>\n" +
                    "\t<fuc2/>\n" +
                    "\t<teste>Isto e content</teste>\n" +
                    "</plano>\n", plano.prettyPrint()
        )
    }

    /**
     * Tests the addition of a global attribute to entities with the specified name.
     */
    @Test
    fun addAttributeGlobalTest() {
        val attributes: MutableMap<String, String> = mutableMapOf()
        attributes.put("global", "2020")
        plano.addAttributeGlobal("curso", "global", "2020")
        assertEquals(attributes, cursoPA.attributes)
    }

    /**
     * Tests the renaming of entities with the specified old name to the new name.
     */
    @Test
    fun renameEntityGlobalTest() {
        plano.renameEntityGlobal("curso", "UC")
        assertEquals("UC", cursoPA.name)
    }

    /**
     * Tests the renaming of attributes across entities with the specified name.
     */
    @Test
    fun renameAttributeNameGlobalTest() {
        componenteQ.addAttribute("nome", "Quizz")
        componenteQ.addAttribute("Peso", "20%")

        componenteP.addAttribute("nome", "Projeto")
        componenteP.addAttribute("Peso", "80%")

        plano.renameAttributeNameGlobal("componente", "nome", "Avaliacao")

        assertTrue(componenteQ.attributes?.get("Avaliacao") == "Quizz")
        assertTrue(componenteP.attributes?.get("Avaliacao") == "Projeto")
    }

    /**
     * Tests the removal of entities with the specified name from the hierarchy.
     */
    @Test
    fun removeEntityGlobalTest() {
        componenteQ.addAttribute("nome", "Quiz")
        componenteQ.addAttribute("Peso", "20%")

        componenteP.addAttribute("nome", "Projeto")
        componenteP.addAttribute("Peso", "80%")

        plano.removeEntityGlobal("componente")

        assertFalse(avaliacao.children.contains(componenteQ))
        assertFalse(avaliacao.children.contains(componenteP))
    }

    /**
     * Tests the removal of attributes from entities with the specified name.
     */
    @Test
    fun removeAttributeGlobalTest() {
        componenteQ.addAttribute("nome", "Quiz")
        componenteQ.addAttribute("Peso", "20%")

        componenteP.addAttribute("nome", "Projeto")
        componenteP.addAttribute("Peso", "80%")

        val componenteX = SelfClosingElement("componente", parent = fucPA)
        componenteX.addAttribute("nome", "Kotlin knowledge")
        componenteX.addAttribute("Peso", "0%")

        plano.removeAttributeGlobal("componente", "Peso")

        assertNull(componenteQ.attributes?.get("Peso"))
        assertNull(componenteP.attributes?.get("Peso"))
        assertNull(componenteX.attributes?.get("Peso"))
    }

    /**
     * Tests the XPath-like search across the hierarchy.
     */
    @Test
    fun xPathTest() {
        assertEquals("<componente/>\n" + "<componente/>\n", plano.xPath("plano/fuc/avaliacao/componente"))
        assertEquals(avaliacao.prettyPrint(), plano.xPath("plano/fuc/avaliacao"))
    }

    @Test
    fun testTranslateNestedEntity() {
        // Act
        val DWN = ParentwithNest("M4A1", "Diretório com Nest", 6.0, "Desgosto")
        // Assert
        val attributes = mutableMapOf<String,String>()
        attributes.put("Código", "M4A1")

        val DirectoryTest = ParentElement("DWithN", null, attributes)
        print(translate(DWN).prettyPrint())
        assertEquals(DirectoryTest, translate(DWN))
    }

    @Test
    fun testTranslateDirectoryWithoutNestedEntities() {


        // Act
        val DwithoutN= ParentwithoutNest("M4A1")

        // Assert
        val attributes = mutableMapOf<String,String>()
        attributes.put("Código", "M4A1")

        val DirectoryTest = ParentElement("DWithoutN", null, attributes)
        print(translate(DwithoutN).prettyPrint())
        assertEquals(DirectoryTest, translate(DwithoutN))
    }

    @Test
    fun testTranslateDirectoryWithNestedEntities() {
        // Arrange
        val directoryObject = object {
            @Anotations.ParentElement("TestDir")
            @Anotations.Attribute("TestAttr")
            val attr: String = "AttrValue"
            @Anotations.SelfClosingElement("TestNested")
            val selfClosingElement: String = "NestedValue"
        }

        // Act
        val result = translate(directoryObject)

        // Assert
        assertTrue(result is ParentElement)
        val parentElement = result as ParentElement
        assertEquals("TestDir", parentElement.name)
        assertNotNull(parentElement.attributes)
        assertEquals("AttrValue", parentElement.attributes?.get("TestAttr"))

        val nestedEntities = parentElement.children.filterIsInstance<SelfClosingElement>()
        assertEquals(1, nestedEntities.size)

        val nestedEntity = nestedEntities.find { it.name == "TestNested" }
        assertNotNull(nestedEntity)
        assertEquals("NestedValue", nestedEntity?.content)
    }

    @Test
    fun testTranslateDirectoryWithParentElementChildren() {
        // Arrange
        val parentObject = object {
            @Anotations.ParentElement("ParentDir")
            val child = object {
                @Anotations.ParentElement("ChildDir")
                @Anotations.Attribute("ChildAttr")
                val attr: String = "ChildAttrValue"
            }
        }

        // Act
        val result = translate(parentObject)

        // Assert
        assertTrue(result is ParentElement)
        val parentEntity = result as ParentElement
        assertEquals("ParentDir", parentEntity.name)

        val childParentElements = parentEntity.children.filterIsInstance<ParentElement>()
        assertEquals(1, childParentElements.size)

        val childEntity = childParentElements.find { it.name == "ChildDir" }
        assertNotNull(childEntity)
        assertNotNull(childEntity?.attributes)
        assertEquals("ChildAttrValue", childEntity?.attributes?.get("ChildAttr"))
    }

    @Test
    fun testTranslateParentElementWithListOfChildrenNested() {
        // Act
        val NWC = SelfClosingWithoutContent("atributo_1")
        val NWC2 = SelfClosingWithoutContent("atributo_2")
        val list = listOf<SelfClosingWithoutContent>(NWC,NWC2)
        val DWLC = ParentwithListOfChildren("M4A1", "Diretório com Nest", 6.0, "Desgosto",list)


        // Assert
        val attributes = mutableMapOf<String,String>()
        attributes.put("Código", "M4A1")
        println(translate(DWLC).prettyPrint())
        val ParentElementTest = ParentElement("DWithListOfChildren", null, attributes)
        assertEquals(ParentElementTest, translate(DWLC))
    }

    @Test
    fun testTranslateParentElementWithListOfChildrenDirectory() {
        // Act
        val NWC = ParentwithNest("M4A1", "Diretório com Nest 1", 4.0, "Desgosto 1")
        val NWC2 = ParentwithNest("M4A2", "Diretório com Nest 2", 5.0, "Desgosto 2")
        val list = listOf<ParentwithNest>(NWC,NWC2)
        val DWLC = ParentwithListOfChildrenDirectory("M4A1", "Diretório com Nest", 6.0, "Desgosto",list)


        // Assert
        val attributes = mutableMapOf<String,String>()
        attributes.put("Código", "M4A1")
        println(translate(DWLC).prettyPrint())
        val ParentElementTest = ParentElement("DWithListOfChildrenDirectory", null, attributes)
        assertEquals(ParentElementTest, translate(DWLC))
    }
}


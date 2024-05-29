package tests
import main.kotlin.Anotations
import main.*
import main.kotlin.ExampleSelfClosingElementAsClass
import main.kotlin.FUCAdapter
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

// Define a FUC class example annotated with ParentElement and XmlAdapter
@Anotations.ParentElement("Fuc")
@Anotations.XmlAdapter(FUCAdapter::class)
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

@Anotations.ParentElement("DWithN")
class ParentwithSelfClosing(
    @Anotations.Attribute("Código")
    val codigo: String,
    @Anotations.SelfClosingElementPrimitive("Nome")
    val nome: String,
    @Anotations.SelfClosingElementPrimitive("Ects")
    val ects: Double,
    @Anotations.SelfClosingElementPrimitive("Observações")
    val observacoes: String,

    )
@Anotations.ParentElement("DWithoutN")
class ParentwithoutNest(
    @Anotations.Attribute("Código")
    val codigo: String,
)

@Anotations.SelfClosingElement("NestedWithContent")
class SelfClosingWithContent(
    @Anotations.Attribute("Código")
    val atributo: String,
    @Anotations.SelfClosingElementContent()
    val conteudo: String,

    )

@Anotations.SelfClosingElement("NestedWithoutContent")
class SelfClosingWithoutContent(
    @Anotations.Attribute("Código")
    val atributo: String,

    )

@Anotations.ParentElement("DWithListOfChildren")
class ParentwithListOfChildren(
    @Anotations.Attribute("Código")
    val codigo: String,
    @Anotations.SelfClosingElementPrimitive("Nome")
    val nome: String,
    @Anotations.SelfClosingElementPrimitive("Ects")
    val ects: Double,
    @Anotations.SelfClosingElementPrimitive("Observações")
    val observacoes: String,
    @Anotations.SelfClosingElement("Avaliacao")
    val avaliacao: List<SelfClosingWithoutContent>
)

@Anotations.ParentElement("DWithListOfChildrenDirectory")
class ParentwithListOfChildrenDirectory(
    @Anotations.Attribute("Código")
    val codigo: String,
    @Anotations.SelfClosingElementPrimitive("Nome")
    val nome: String,
    @Anotations.SelfClosingElementPrimitive("Ects")
    val ects: Double,
    @Anotations.SelfClosingElementPrimitive("Observações")
    val observacoes: String,
    @Anotations.ParentElement("Avaliacao")
    val avaliacao: List<ParentwithSelfClosing>
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
        plano.addSelfClosingElementEntity("testEntity")
        assertFalse(plano.children.contains(testEntity))
    }

    /**
     * Tests the addition of a directory entity to another directory entity.
     */
    @Test
    fun addDirectoryEntityTest() {
        val testEntity = SelfClosingElement("testEntity")
        plano.addParentElementEntity("testEntity")
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
        plano.addSelfClosingElementEntity(entityName = "teste", entityContent = "Isto e content")
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

    /**
     * Tests the translation of an object to a single SelfClosingElement.
     */
    @Test
    fun testTranslateSelfClosingElement() {
        // Act
        val SCE = SelfClosingWithoutContent("M4A1")
        // Assert
        val attributes = mutableMapOf<String,String>()
        attributes.put("Código", "M4A1")

        val parentElementTest = SelfClosingElement("NestedWithoutContent",null,null,attributes)
        print(translate(SCE).prettyPrint())
        assertEquals(parentElementTest, translate(SCE))
    }

    /**
     * Tests the translation of an object to a ParentElement with only attributes.
     */
    @Test
    fun testTranslateParentElementWithOnlyAttribute() {


        // Act
        val parentWithoutSelfClosing= ParentwithoutNest("M4A1",)

        // Assert
        val attributes = mutableMapOf<String,String>()
        attributes.put("Código", "M4A1")

        val parentElementTest = ParentElement("DWithoutN", null, attributes)
        print(translate(parentWithoutSelfClosing).prettyPrint())
        assertEquals(parentElementTest, translate(parentWithoutSelfClosing))
    }

    /**
     * Tests the translation of an object to a ParentElement with SelfClosing Children.
     */
    @Test
    fun testTranslateParentElementWithSelfClosingChildren() {
        // Act
        val pWSC = ParentwithSelfClosing("M4A1", "ParentElement com SelfClosing", 6.0, "gosto")
        // Assert
        val attributes = mutableMapOf<String,String>()
        attributes.put("Código", "M4A1")

        val parentElementTest = ParentElement("DWithN", null, attributes)
        print(translate(pWSC).prettyPrint())
        assertEquals(parentElementTest, translate(pWSC))
    }

    /**
     * Tests the translation of an object to a ParentElement with SelfClosingElement Children in a property list.
     */
    @Test
    fun testTranslateParentElementWithListOfChildrenSelfClosing() {
        // Act
        val sCWC = SelfClosingWithoutContent("atributo_1")
        val sCWC2 = SelfClosingWithoutContent("atributo_2")
        val list = listOf<SelfClosingWithoutContent>(sCWC,sCWC2)
        val pWLC = ParentwithListOfChildren("M4A1", "Diretório com Nest", 6.0, "Desgosto",list)


        // Assert
        val attributes = mutableMapOf<String,String>()
        attributes.put("Código", "M4A1")
        println(translate(pWLC).prettyPrint())
        val parentElementTest = ParentElement("DWithListOfChildren", null, attributes)
        assertEquals(parentElementTest, translate(pWLC))
    }

    /**
     * Tests the translation of an object to a ParentElement with ParentElement Children in a property list.
     */
    @Test
    fun testTranslateParentElementWithListOfChildrenDirectory() {
        // Act
        val pWSC = ParentwithSelfClosing("M4A1", "Parent com Nest 1", 4.0, "gosto 1")
        val pWSC2 = ParentwithSelfClosing("M4A2", "Parent com Nest 2", 5.0, "gosto 2")
        val list = listOf<ParentwithSelfClosing>(pWSC,pWSC2)
        val pWLC = ParentwithListOfChildrenDirectory("M4A1", "Parent com Nest", 6.0, "gosto",list)


        // Assert
        val attributes = mutableMapOf<String,String>()
        attributes.put("Código", "M4A1")
        println(translate(pWLC).prettyPrint())
        val parentElementTest = ParentElement("DWithListOfChildrenDirectory", null, attributes)
        assertEquals(parentElementTest, translate(pWLC))
    }
}


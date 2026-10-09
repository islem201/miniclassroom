package com.example.miniclassroom

data class Subject(val id: Int, val name: String, val emoji: String)
data class Course(val id: Int, val subjectId: Int, val title: String, val content: String)
data class TodoItem(val id: Int, val title: String, val done: Boolean = false)
data class Submission(val title: String, val subject: String, val dueDate: String, val status: String)

object FakeData {
    val subjects = listOf(
        Subject(1, "Mathématiques", "📐"),
        Subject(2, "Physique", "⚛️"),
        Subject(3, "Informatique", "💻"),
        Subject ( 4, "Droit","⚖\uFE0F" )
    )

    val courses = listOf(
        Course(1, 1, "Les dérivées", "La dérivée d'une fonction mesure la vitesse à laquelle sa valeur change. Géométriquement, c'est la pente de la tangente à la courbe. Pour f(x) = x², la dérivée est f'(x) = 2x."),
        Course(2, 1, "Les probabilités", "Une probabilité est un nombre entre 0 et 1 qui mesure la chance qu'un événement se produise. Pour un dé équilibré, la probabilité d'obtenir 6 est 1/6."),
        Course(3, 2, "Les lois de Newton", "La première loi dit qu'un objet sans force reste au repos ou en mouvement uniforme. La deuxième loi énonce F = m × a. La troisième dit que toute action provoque une réaction égale et opposée."),
        Course(4, 2, "L'énergie", "L'énergie ne se crée pas et ne se détruit pas, elle se transforme. L'énergie cinétique vaut Ec = ½mv² et l'énergie potentielle vaut Ep = mgh."),
        Course(5, 3, "Les variables en Kotlin", "Kotlin utilise 'val' pour une variable immuable et 'var' pour une variable modifiable. Un type suivi de '?' peut contenir null, ce qui évite de nombreux plantages."),
        Course(6, 3, "Les boucles", "Une boucle répète un bloc de code. 'for' parcourt une collection, 'while' répète tant qu'une condition est vraie. Attention aux boucles infinies.")
    )
}

val fakeSubmissions = listOf(
    Submission("Exercices sur les dérivées", "Mathématiques", "12 oct.", "Rendu"),
    Submission("TP Lois de Newton", "Physique", "15 oct.", "À rendre"),
    Submission("Mini-projet Kotlin", "Informatique", "20 oct.", "À rendre"),
    Submission("Devoir probabilités", "Mathématiques", "05 oct.", "En retard")
)
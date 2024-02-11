package com.example.epsi_quiz_application

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.io.Serializable
import com.example.epsi_quiz_application.ui.theme.Epsi_Quiz_ApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Epsi_Quiz_ApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomePage(this)
                }
            }
        }
    }
}

data class Quiz(
    val question: String,
    val options: List<String>,
    val correctAnswer: String
) : Serializable

val quiz1 = Quiz(
    question = "Qu’est ce que le Cloud Computing ?",
    options = listOf("Une nouvelle marque d’ordinateur portable", "Une technologie permettant de stocker et d’accéder à des données via Internet", "Un type de café très populaire parmi les informaticiens", "Un logiciel antivirus très performante"),
    correctAnswer = "Une technologie permettant de stocker et d’accéder à des données via Internet"
)

val quiz2 = Quiz(
    question = "Qu'est-ce qu'un algorithme de tri ?",
    options = listOf("Un ensemble d'étapes systématiques utilisé pour organiser les éléments d'une collection de données dans un ordre spécifié", "Un moyen de trier ses déchets.", "Un algorithme permettant de ranger des éléments de manière aléatoire.", "Un tout nouveau réseau social pour discuter avec des personnes en ligne."),
    correctAnswer = "Un ensemble d'étapes systématiques utilisé pour organiser les éléments d'une collection de données dans un ordre spécifié"
)

val quiz3 = Quiz(
    question = "Qu'est-ce que le développement mobile ?",
    options = listOf("C'est le processus de fabrication de téléphones portables.", "Une entreprise spécialisée dans l'achat et la revente de téléphones mobiles.", "Un nouveau moyen de transport révolutionnaire.", "C'est la création d'applications logicielles spécifiquement conçues pour être exécutées sur des appareils mobiles tels que les smartphones et les tablettes"),
    correctAnswer = "C'est la création d'applications logicielles spécifiquement conçues pour être exécutées sur des appareils mobiles tels que les smartphones et les tablettes"
)

@Composable
fun HomePage(activity: ComponentActivity) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(color = Color.LightGray),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "BinaryQuiz !",
            fontSize = 64.sp,
            modifier = Modifier.padding(bottom = 75.dp)
        )
        Text(
            text = "Bienvenue sur BinaryQuiz, où chaque question est une aventure en attente de votre réponse !",
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Text(
            text = "Choisissez votre BinaryQuiz :",
            fontSize = 28.sp,
            modifier = Modifier.padding(bottom = 48.dp)
        )

        QuizButton("CloudCodingQuiz", 1, activity)
        QuizButton("AlgorithmQuiz", 2, activity)
        QuizButton("MobileMasterQuiz", 3, activity)
    }
}

@Composable
fun QuizButton(text: String, quizNumber: Int, activity: ComponentActivity) {
    var isVisible by remember { mutableStateOf(true) }

    if (isVisible) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = {
                    navigateToQuiz(quizNumber, activity)
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = 16.dp)
            ) {
                Text(text = text)
            }

            IconButton(
                onClick = {
                    isVisible = false
                },
                modifier = Modifier.padding(end = 16.dp)
            ) {
                Icon(Icons.Filled.Close, contentDescription = "Supprimer")
            }
        }
    }
}


fun navigateToQuiz(quizNumber: Int, activity: ComponentActivity) {
    val selectedQuiz = when (quizNumber) {
        1 -> quiz1
        2 -> quiz2
        3 -> quiz3
        else -> null
    }

    selectedQuiz?.let {
        val intent = Intent(activity, QuizActivity::class.java).apply {
            putExtra("quiz", it)
        }
        activity.startActivity(intent)
    }
}

class QuizActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val quiz = intent.getSerializableExtra("quiz") as Quiz
        setContent {
            Epsi_Quiz_ApplicationTheme {
                QuizActivity(quiz, this@QuizActivity)
            }
        }
    }
}

@Composable
fun QuizActivity(quiz: Quiz, activity: ComponentActivity) {
    var selectedOption by remember { mutableStateOf<String?>(null) }
    var answered by remember { mutableStateOf(false) }
    var isCorrect by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Question: ${quiz.question}", modifier = Modifier.padding(bottom = 8.dp))
        quiz.options.forEach { option ->
            if (!answered || option == selectedOption || option == quiz.correctAnswer) {
                Button(
                    onClick = {
                        if (!answered) {
                            selectedOption = option
                            answered = true
                            isCorrect = option == quiz.correctAnswer
                        }
                    },
                    modifier = Modifier.padding(bottom = 8.dp),
                    enabled = !answered,
                    colors =
                    if (answered && option == selectedOption) {
                        if (isCorrect) ButtonDefaults.buttonColors(contentColor = Color.Green)
                        else ButtonDefaults.buttonColors(contentColor = Color.Red)
                    } else if (answered && option == quiz.correctAnswer) {
                        ButtonDefaults.buttonColors(contentColor = Color.Green)
                    } else {
                        ButtonDefaults.buttonColors()
                    }
                ) {
                    Text(text = option)
                }
            }
        }
        if (answered) {
            Text(
                text = if (isCorrect) "Bonne réponse !" else "Mauvaise réponse ! La bonne réponse est : ${quiz.correctAnswer}",
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .background(color = if (isCorrect) Color.Green else Color.Red),
            )
            Button(
                onClick = {
                    activity.startActivity(Intent(activity, MainActivity::class.java))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White
                )
            ) {
                Text(text = "Retour à l'accueil")
            }
        }
    }
}



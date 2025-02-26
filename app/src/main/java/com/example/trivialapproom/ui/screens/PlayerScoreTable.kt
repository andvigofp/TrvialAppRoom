package com.example.trivialapproom.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun PlayerScoreTable(playerScores: List<PlayerScore>) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Scores", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        // Table header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Player", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
            Text("Category", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
            Text("Score", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
        }

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        // Table content
        playerScores.forEach { playerScore ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(playerScore.player, style = MaterialTheme.typography.bodyMedium)
                Text(playerScore.category, style = MaterialTheme.typography.bodyMedium)
                Text("${playerScore.score}%", style = MaterialTheme.typography.bodyMedium)
            }
            Divider(modifier = Modifier.padding(vertical = 8.dp))
        }
    }
}

// Data class to represent player scores
data class PlayerScore(
    val player: String,
    val category: String,
    val score: Int
)

// Preview for the PlayerScoreTable
@Preview(showBackground = true)
@Composable
fun PlayerScoreTablePreview() {
    val sampleScores = listOf(
        PlayerScore("Alice", "Science", 85),
        PlayerScore("Bob", "History", 90),
        PlayerScore("Charlie", "Geography", 70)
    )

    PlayerScoreTable(playerScores = sampleScores)
}

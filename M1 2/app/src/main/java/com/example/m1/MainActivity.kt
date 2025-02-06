package com.example.m1

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.m1.ui.theme.M1Theme
//Programmer: Yeabsera Damte
//Date: 10/02/2024
//Android Studio Koala | 2024.1.1
//macOS Sonoma 14.4.1
//Description: This app is an interactive Tic-Tac-Toe game where the user plays with the machine.
//There is a home screen that allows the user to choose who goes first (the user or the machine).
//The app is programmed so that the machine never loses, in other words the machine either wins or ties with the user.
//It makes a toast showing the winner and allows the user to play again, returning to the home screen.
//Citations: For Algorithms and Ideas for Design Structures =>
//https://en.wikipedia.org/wiki/Minimax
//https://www.geeksforgeeks.org/minimax-algorithm-in-game-theory-set-1-introduction/
//https://developer.android.com/develop/ui/compose/documentation


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            M1Theme {
                MainApplication()
            }
        }
    }
}

@Composable
fun MainApplication() {
    var begin by remember { mutableStateOf(true)}
    var humanStart by remember { mutableStateOf(true)}

    if(begin){
        HomeScreen(
            startHuman = {
                humanStart = true
                begin = false
            },
            startComputer = {
                humanStart = false
                begin = false
            }
        )
    }else{
        TicTacToe(humanStart) {
            begin = true
        }
    }
}

@Composable
fun HomeScreen(startHuman: () -> Unit, startComputer: () -> Unit){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(255, 229, 180))
            .padding(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Text(
            text = "Tic-Tac-Toe with Machine",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = startHuman){
            Text(text = "Human First")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = startComputer){
            Text(text = "Machine First")
        }
    }
}

@Composable
fun TicTacToe(humanFirst: Boolean, onNewGame: () -> Unit) {
    var grid by remember {mutableStateOf(Array(3) {Array(3){""}} )}
    var playerTurn by remember {mutableStateOf(true)}
    var scoreStatus by remember {mutableStateOf("Turn: Human (X)")}

    val context = LocalContext.current
    var gameOver by remember { mutableStateOf(false) }



    LaunchedEffect(Unit){
        if(!humanFirst){
            machineMove(grid){newBoard ->
                grid = newBoard
                scoreStatus = "Turn: X"
                playerTurn = true
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(255, 229, 180))
            .padding(15.dp),
        horizontalAlignment =Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Text(text = scoreStatus, style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(25.dp))

        Box(
            modifier = Modifier
                .size(320.dp)
                .padding(10.dp)
        ){
            for(index in 1..2){
                Divider(
                    color = Color.Black,
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(5.dp)
                        .align(Alignment.CenterStart)
                        .offset(x = ((index * 100).dp))
                )
                Divider(
                    color = Color.Black,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(5.dp)
                        .align(Alignment.TopCenter)
                        .offset(y = ((index * 100).dp))
                )
            }

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                for(x in 0..2){
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ){
                        for(y in 0..2){
                            Box(
                                modifier = Modifier
                                    //.size(100.dp)
                                    //.padding(5.dp)
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .clickable{
                                        if(grid[x][y].isEmpty() && playerTurn && !gameOver){
                                            grid[x][y] = "X"
                                            if(test(grid)){
                                                scoreStatus = "Human (X) wins"
                                                //board = Array(3) {Array(3) {""} }
                                                Toast.makeText(context, "Human (X) wins", Toast.LENGTH_SHORT).show()
                                                //onGameEnd()
                                                gameOver = true
                                            } else if(isFull(grid)){
                                                scoreStatus = "Tie Game!"
                                                //board = Array(3){Array(3){""} }
                                                Toast.makeText(context, "It's a tie!", Toast.LENGTH_SHORT).show()
                                                //onGameEnd()
                                                gameOver = true
                                            }else{
                                                playerTurn = false
                                                scoreStatus = "Turn: Machine (O)"



                                                //LaunchedEffect(Unit) {
                                                    machineMove(grid) {newBoard ->
                                                        //board = it
                                                        grid = newBoard

                                                        if(test(grid)){
                                                            scoreStatus = "Machine (O) wins"
                                                            //board = Array(3) {Array(3) {""} }
                                                            Toast.makeText(context, "Machine (O) wins", Toast.LENGTH_SHORT).show()
                                                            //onGameEnd()
                                                            gameOver = true
                                                        }else if(isFull(newBoard)){
                                                            scoreStatus = "Tie Game!"
                                                            //board = Array(3) {Array(3) {""} }
                                                            Toast.makeText(context, "It's a tie!", Toast.LENGTH_SHORT).show()
                                                            //onGameEnd()
                                                            gameOver = true
                                                        }else{
                                                            playerTurn = true
                                                            scoreStatus = "Turn: Human (X)"
                                                        }
                                                    }
                                                //}

                                            }
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ){
                                Text(text = grid[x][y], style = MaterialTheme.typography.displayLarge, color = Color.Black)
                            }
                        }
                    }
                }
            }
        }

        if(gameOver){
            Spacer(modifier = Modifier.height(30.dp))

            Button(onClick = onNewGame){
                Text(text = "Play Again")
            }
        }
    }
}


private fun machineMove(grid: Array<Array<String>>, onNewGrid: (Array<Array<String>>) -> Unit){
    val newGameGrid = grid.map{it.copyOf()}.toTypedArray()
    val winMove = findWinMove(newGameGrid, "O")

    if(winMove != null){
        newGameGrid[winMove.first][winMove.second] = "O"
        onNewGrid(newGameGrid)
        return
    }

    val humanWinMove = findWinMove(newGameGrid, "X")

    if(humanWinMove != null){
        newGameGrid[humanWinMove.first][humanWinMove.second] = "O"
        onNewGrid(newGameGrid)
        return
    }

    val nextBestMove = nextMove(newGameGrid)

    if(nextBestMove != null){
        newGameGrid[nextBestMove.first][nextBestMove.second] = "O"
        onNewGrid(newGameGrid)
    }


}

private fun findWinMove(grid: Array<Array<String>>, user: String): Pair<Int, Int>? {
    for(x in 0..2){
        for(y in 0..2){
            if(grid[x][y].isEmpty()){
                grid[x][y] = user

                if(test(grid)){
                    grid[x][y] = ""
                    return Pair(x,y)
                }

                grid[x][y] = ""
            }
        }
    }

    return null
}


private fun nextMove(grid: Array<Array<String>>): Pair<Int, Int>? {
    var move: Pair<Int, Int>? = null
    var value = Int.MIN_VALUE

    for(x in 0..2){
        for(y in 0..2){
            if(grid[x][y].isEmpty()){
                grid[x][y] = "O"

                val moveVal = minMaxAlg(grid, false)

                grid[x][y] = ""

                if(moveVal > value){
                    move = Pair(x,y)
                    value = moveVal
                }
            }
        }
    }

    return move
}

private fun minMaxAlg(grid: Array<Array<String>>, isMax: Boolean): Int{
    if(test(grid)){
        return if (isMax) -10 else 10
    }

    if(isFull(grid)){
        return 0
    }

    return if(isMax){
        var value = Int.MIN_VALUE

        for(x in 0 ..2){
            for(y in 0..2){
                if(grid[x][y].isEmpty()){
                    grid[x][y] = "O"

                    value = maxOf(value, minMaxAlg(grid, false))
                    grid[x][y] = ""
                }
            }
        }
        value
    }else{
        var value = Int.MAX_VALUE

        for(x in 0..2){
            for(y in 0..2){
                if(grid[x][y].isEmpty()){
                    grid[x][y] = "X"
                    value = minOf(value, minMaxAlg(grid, true))
                    grid[x][y] = ""
                }
            }
        }

        value
    }
}

private fun test(grid: Array<Array<String>>): Boolean{
    for (index in 0..2){
        if(grid[index][0] == grid[index][1] && grid[index][1] == grid[index][2] && grid[index][0].isNotEmpty()) return true
        if(grid[0][index] == grid[1][index] && grid[1][index] == grid[2][index] && grid[0][index].isNotEmpty()) return true
    }

    if(grid[0][0] == grid[1][1] && grid[1][1] == grid[2][2] && grid[0][0].isNotEmpty()) return true
    if(grid[0][2] == grid[1][1] && grid[1][1] == grid[2][0] && grid[0][2].isNotEmpty()) return true

    return false
}

private fun isFull(grid: Array<Array<String>>): Boolean{
    for (x in grid){
        for(entry in x){
            if(entry.isEmpty()) return false
        }
    }

    return true
}


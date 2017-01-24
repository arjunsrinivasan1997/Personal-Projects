from random import randint

board = []

for x in range(6):
    board.append(["O"] * 6)

def print_board(board):
    for row in board:
        print('   '.join(row))

print ("Let's play Battleship!")
print ("The board is a 6x6 board")
print ("Here is the board")
print_board(board)
print ("There are two battleships hidden at two different coordinates")
print ("Each Battleship takes up one space on the board")
print ("You will have 10 turns to find the ships")
print ("The rows are numbered 1-6, so the first row on the left is row 1 and the last row is row 6")
print ("the columns are numbered 1-6, so the first column one the left is column 1 and the last column is 6")
print ("For Example, If you thought the battleship was in row 1, column 4 this is where  you would be guessing")
board[0][3]="X"
print_board(board)
board[0][3]="O"



def random_row(board):
    return randint(0, len(board) - 1)

def random_col(board):
    return randint(0, len(board) - 1)

ship_row = random_row(board)
ship_col = random_col(board)
ship_row1 = random_row(board)
ship_col1 = random_col(board)

while ship_row==ship_row1 and ship_col==ship_col1:
    ship_row1==0
    ship_col1==0
    ship_row1 = random_row(board)
    ship_col1 = random_col(board)
turn=0
for a in range(100000000000):
    guess_row = int(input("Guess Row of battleship:"))-1
    guess_col = int(input("Guess Column of batleship:"))-1
    
    if (guess_row < 0 or guess_row > 5) or (guess_col < 0 or guess_col > 5):
            print ("Oops, that's not even in the ocean.")
            print_board(board)
            print ("That was turn",turn)
            print ("You have", 10-turn, "Turns remaining")
    elif(board[guess_row][guess_col] == "X") or board[guess_row][guess_col] == "B":
            print ("You guessed that one already.")
            print_board(board)
            print ("That was turn",turn)
            print ("You have", 10-turn, "Turns remaining")
        
    elif (guess_row == ship_row and guess_col==ship_col) or (guess_row == ship_row1 and guess_col==ship_col1):
        print ("Congratulations! You sunk a battleship!")
        board[guess_row][guess_col]="B"
        print_board(board)
        turn=turn+1
        if board[ship_row][ship_col]=="B" and board[ship_row1][ship_col1]=="B":
            print ("Congratulations! You won the game :D")
            break
        else:
            print ("That was turn",turn)
            print ("You have", 10-turn, "Turns remaining")
    else:
        print ("You missed my battleship!")
        board[guess_row][guess_col] = "X"
        turn=turn+1
        print_board(board)
        print ("That was turn",turn)
        print ("You have", 10-turn, "Turns remaining")
        if turn==10:
            print ("Game Over")
            if board[ship_row][ship_col]=="B":
                print ("The other battleship was in row ", ship_row1+1," and in column", ship_col1+1)
                board[ship_row1][ship_col1]="B"
                print_board(board)
            elif board[ship_row1][ship_col1]=="B":
                print ("The other battleship was in row ", ship_row1+1," and in column", ship_col1+1)
                board[ship_row][ship_col]="B"
                print_board(board)
            else:
                print ("You didn't sink any of the battleships")
                print ("One battleship was in row ", ship_row1+1," and in column", ship_col1+1)
                print ("The other battleship was in row", ship_row+1," and in column", ship_col+1)
                board[ship_row][ship_col]="B"
                board[ship_row1][ship_col1]="B"
                print_board(board)
            break
        
            
        
            
    
    

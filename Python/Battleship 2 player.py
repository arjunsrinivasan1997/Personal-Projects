from random import randint

p1board = []
p2board=[]



for x in range(6):
    p1board.append(["O"]*6+[str(x+1)])
    p2board.append(["O"]*6)
p1board.append([str(x) for x in range(1,7)])
p2board.append([str(x) for x in range(1,7)])

def print_board(board):
    for row in board:
        print('   '.join(row))

print("Let's play head to head battleship")
Player1=input("Player 1, type your name here:")
Player2=input("Player 2 type your name here:")
print ("The board is a 6x6 board")
print ("Here is the board")
print_board(p1board)
print ("The rows are numbered 1-6, so the first row on the left is row 1 and the last   row is row 6")
print ("the columns are numbered 1-6, so the first column one the left is column 1 and  the last column is 6")
print ("For Example, If you thought the battleship was in row 1, column 4 this is where  you would be guessing")
p1board[0][3]="X"
print_board(p1board)
p1board[0][3]="O"
print ("The battleships are randomly assigned on each players board")
p1row=randint(0,5)
p1column=randint(0,5)
p2row=randint(0,5)
p2column=randint(0,5)
print("The game is ready to begin")
for y in range(37):
    print("%s it is your turn" %(Player1))
    print("Here is your previous guesses")
    print_board(p1board)
    guess_row1 = int(input("Guess Row of battleship:"))-1
    guess_col1 = int(input("Guess Column of batleship:"))-1
    if (guess_row1<0 or guess_row1>5) or (guess_col1<0 or guess_row1>5):
        print("Oops, that is not even in the ocean")
        guess_row1 = int(input("Guess Row of battleship:"))-1
        guess_col1 = int(input("Guess Column of batleship:"))-1
    elif(p1board[guess_row1][guess_col1] == "X"):
        print ("You guessed that one already.")
        print_board(p1board)
    elif(guess_row1==p2row and guess_col1==p2column):
        print("Congratulations. You sunk %s's battleship" % Player2)
        break
    else:
        print("You missed %s's battleship" % Player2)
        p1board[guess_row1][guess_col1]="X"
        'player 2 turn'
        for t in range(1):
            print("%s it is your turn" %(Player2))
            print("Here is your previous guesses")
            print_board(p2board)
            guess_row1 = int(input("Guess Row of battleship:"))-1
            guess_col1 = int(input("Guess Column of batleship:"))-1
            if (guess_row1<0 or guess_row1>5) or (guess_col1<0 or guess_row1>5):
                print("Oops, that is not even in the ocean")
                guess_row1 = int(input("Guess Row of battleship:"))-1
                guess_col1 = int(input("Guess Column of batleship:"))-1
            elif p2board[guess_row1][guess_col1] == "X":
                print ("You guessed that one already.")
                print_board(p2board)
            elif(guess_row1==p1row and guess_col1==p1column):
                print("Congratulations. You sunk %s's battleship" % Player1)
                break
            else:
                print("You missed %s's battleship" % Player1)
                p2board[guess_row1][guess_col1]="X"
                

                    
            
    
        
    
    
    


#Helper Variables#
player1_piece="R"
player2_piece="B"
empty_piece="O"
############################################################
#Helper Classes#
#Class for Players#
class Player():
    def __init__(self,name,piece):
        self.name = name
        self.piece = piece
    
#Class for boards, Includes methods for placing pieces on boards and checking to see if a connect 4 has been achieved#
class Board():
    def __init__(self,rows,columns):
        self.rows = rows
        self.columns = columns
        self.board = [[empty_piece]* self.columns for x in range(self.rows)]+[[str(x) for x in range(1,8)]]       
    def __str__(self):
        """Method for displaying Board"""
        length = len(self.board)
        for a in range(length - 1):
            print('   '.join(self.board[a]))
        return '   '.join(self.board[length - 1])
    def __repr__(self):
        print("This board has",self.rows,"rows and", self.columns, "columns")
        return str(self.board)
    def put_piece(self,column,piece,opponent_piece,empty_piece=empty_piece):
        
        if self.board[0][column] == piece or self.board[0][column] == opponent_piece:
            raise SyntaxError("Column is Full")
        elif self.board[5][column] == "O":
            self.board[5][column]=piece
            return
            #return print(board)
        for a in reversed(range(0,6)):
            if (self.board[a][column] == piece or self.board[a][column] == opponent_piece) and (self.board[a-1][column] == empty_piece):
                self.board[a-1][column] = piece
                return
                #return print(board)
    def check_win_vertical(self,empty_piece=empty_piece):
        """If four pieces in a column are identical returns True. Otherwise returns False"""
        for c in range(0,self.columns):
            for a in reversed(range(0,self.rows)):
                try:
                    if self.board[a][c] == self.board[a-1][c] == self.board[a-2][c] == self.board[a-3][c] and self.board[a][c] != empty_piece:
                        return True
                except IndexError:
                    pass
        return False
    def check_win_horizontal(self,empty_piece=empty_piece):
        'If four pieces in a row are identical retruns True. Otherwise returns False'
        for c in range(0,self.rows):
            for a in range(0,self.columns):
                try:
                    if self.board[c][a] == self.board[c][a+1] == self.board[c][a+2] == self.board[c][a+3] and self.board[c][a] != empty_piece:
                        return True
                except IndexError:
                    pass
        return False
    def check_win_diagonal(self, empty_piece=empty_piece):
        'If four pieces diagonally are identical returns True. Otherwise returns False'
        #column 0
        for a in reversed(range(0,self.rows)):
            try:
                if self.board[a][0] == self.board[a-1][1] == self.board[a-2][2] == self.board[a-3][3] and self.board[a][0] != empty_piece:
                    return True
            except IndexError:
                pass
            
        #column 1
        for a in reversed(range(0,self.rows)):
            try:
                if self.board[a][1] == self.board[a-1][2] == self.board[a-2][3] == self.board[a-3][4] and self.board[a][1] != empty_piece:
                    return True
            except IndexError:
                pass
            try:
                if self.board[a+1][0] == self.board[a][1] == self.board[a-1][2] == self.board [a-2][3] and self.board[a][1] != empty_piece:
                    return True
            except IndexError:
                pass
        #column 2
        for a in reversed(range(0,self.rows)):
            try:
                if self.board[a][2] == self.board[a-1][3] == self.board[a-2][4] == self.board[a-3][5] and self.board[a][2] != empty_piece:
                    return True
            except IndexError:
                pass
            try:
                if self.board[a+1][1] == self.board[a][2] == self.board[a-1][3] == self.board [a-2][4] and self.board[a][2] != empty_piece:
                    return True
            except IndexError:
                pass                
        #column 3 
        for a in reversed(range(0,self.rows)):
            try:
                if self.board[a][3] == self.board[a-1][4] == self.board[a-2][5] == self.board[a-3][6] and self.board[a][3] != empty_piece:
                    return True
                elif self.board[a][3] == self.board[a-1][2] == self.board[a-2][1] == self.board[a-3][0] and self.board[a][3] != empty_piece:
                    return True
            except IndexError:
                pass            
        #column 4
        for a in reversed(range(0,self.rows)):
            try:
                if self.board[a][4] == self.board[a-1][3] == self.board[a-2][2] == self.board[a-3][1] and self.board[a][4] != empty_piece:
                    return True
            except IndexError:
                pass
            try:
                if self.board[a+1][5] == self.board[a][4] == self.board[a-1][3] == self.board [a-2][2] and self.board[a][4] != empty_piece:
                    return True
            except IndexError:
                pass
        #column 5
        for a in reversed(range(0,self.rows)):
            try:
                if self.board[a][5] == self.board[a-1][4] == self.board[a-2][3] == self.board[a-3][2] and self.board[a][5] != empty_piece:
                    return True
            except IndexError:
                pass
            try:
                if self.board[a+1][6] == self.board[a][5] == self.board[a-1][4] == self.board [a-2][3] and self.board[a][5] != empty_piece:
                    return True
            except IndexError:
                pass
        #column 6
        for a in reversed(range(0,self.rows)):
            try:
                if self.board[a][6] == self.board[a-1][5] == self.board[a-2][4] == self.board[a-3][3] and self.board[a][6] != empty_piece:
                    return True
            except IndexError:
                pass
        return False
############################################################



def take_turn(player1,player2,board):
    column = int(input("What column would you like to place your piece in"))
    column -= 1
    try:    
        board.put_piece(column,player1.piece,player2.piece,empty_piece)
    except SyntaxError:
        print("That column is full. PLease pick another column")
        column = int(input("What column would you like to place your piece in"))
        column -= 1
    print(board)
    if board.check_win_vertical() or board.check_win_horizontal() or board.check_win_diagonal():
        return True
    return False                                            
board=Board(6,7)
print("Lets play connect 4!")
print("Here is the board")
print(board)
print("To place your piece on the board, type the column in which you would like the piece to be placed in")
print("The first column on the left is column number 1, and the last column on the right is column number 7")
player1=Player(str(input("Player 1, type your name here:")),player1_piece)
player2=Player(str(input("Player 2, type your name here:")),player2_piece)
print("%s your pieces will be marked by an %s on the board"%(player1.name, player1.piece))
print("%s your pieces will be marked by an %s on the board"%(player2.name, player2.piece))
connect4=False
while connect4 is False:
    print("%s it is your turn"%(player1.name))
    connect4 = take_turn(player1,player2,board)
    if connect4:
        print("Congraulations %s. You have won the game"%(player1.name))
        connect4 = True
    print("%s it is your turn"%(player2.name))
    connect4 = take_turn(player2,player1,board)
    if connect4:
        print("Congraulations %s. You have won the game"%(player2.name))
        connect4 = True

        
    
        

        
        
            
    
        
        


    

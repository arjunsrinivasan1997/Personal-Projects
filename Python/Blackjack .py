def blackjack():
        """Plays a text based version of blackjack"""
        print("Let's play Blackjack")
        print("Blackjack pays 3 to 2")
        print("The computer will act as the dealer")
        m=1000000
        starting_amount=m
        print("You will be given $%s to start with"%("{:,}".format(m)))
        player_value=0
        player_value1=0
        total=0
        total1=0
        ctotal=0
        ctotal1=0
        list_of_cards=[]
        player_wins=0
        computer_wins=0
        draws=0
        from random import shuffle, randint
        while m>0:
                total_games=player_wins+computer_wins+draws
                try:
                        print("You have played",computer_wins+player_wins+draws,"games","and have won",round(player_wins/total_games*100,3),"% of the time")
                except ZeroDivisionError:
                        pass
                if m>starting_amount:
                        print("Overall, You have won $%s"%("{:,}".format(int((m-starting_amount)))))
                elif m<starting_amount:
                        print("Overall, You have lost $%s"%("{:,}".format(int((starting_amount-m)))))
                if not list_of_cards:
                        for c in range(6):
                                for a in range(4):
                                    list_of_cards.append("A")
                                    list_of_cards.append("K")
                                    list_of_cards.append("Q")
                                    list_of_cards.append("J")
                                for a in range(2,11):
                                    for b in range(4):
                                            list_of_cards.append(a)

                        shuffle(list_of_cards)
                total=0
                total1=0
                ctotal=0
                ctotal1=0
                print("You have $%s remaining" %("{:,}".format(int(m))))
                a=1
                while a==1:
                        try:
                                w=int(input("How much would you like to wager for this round:"))
                                a=0
                        except ValueError:
                                a=1
                while w>m:
                    print("You don't have that much money")
                    w=int(input("How much would you like to wager for this round:"))
                for a in range(100):
                    b=randint(0,len(list_of_cards)-1)
                    print('You drew a ',list_of_cards[b])
                    if list_of_cards[b]=="K" or list_of_cards[b]=="Q" or list_of_cards[b]=="J":
                        player_value=10
                        if total1>0:
                            total=total+player_value
                            total1=total1+player_value
                        elif total1==0:
                            total=total+player_value
                    elif list_of_cards[b]=="A":
                        player_value=1
                        player_value1=11
                        total=total+player_value
                        total1=total1+player_value1
                    else:
                        player_value=int(list_of_cards[b])
                        if total1>0:
                            total=total+player_value
                            total1=total1+player_value
                        elif total1==0:
                            total=total+player_value
                    list_of_cards.remove(list_of_cards[b])
                    if total1>21 and total<21 and total!=0:
                        total1=0
                    if total>21 and total1<21 and total1!=0:
                        total=0

                    if total1!=0 and total1!=total:
                        print("Your total is",total,"or",total1)
                    else:
                        print("Your total is",total)
                    if total==21 or total1==21:
                        print("Congratulations, you got Blackjack!")
                        m=m+(w*3)/2
                        player_wins=player_wins+1
                        break
                    if total>21 or total1>21:
                        print("You went bust")
                        m=m-w
                        computer_wins=computer_wins+1
                        break
                    if total1==0:
                        total1=total
                    a=input("Would you like to hit or to stay? Type h to hit and s to stay:")
                    while a != 'h' and a != 's': 
                            a=input("Would you like to hit or to stay? Type h to hit and s to stay:")
                    while a=='h':
                        b=randint(0,len(list_of_cards)-1)
                        print('You drew a ',list_of_cards[b])
                        if list_of_cards[b]=="K" or list_of_cards[b]=="Q" or list_of_cards[b]=="J":
                            player_value=10
                            if total1>0:
                                total=total+player_value
                                total1=total1+player_value
                            elif total1==0:
                                total=total+player_value
                        elif list_of_cards[b]=="A":
                            player_value=1
                            player_value1=11
                            if total1+11>21:
                                   total=total+player_value
                                   total1=0
                            else:
                                total=total+player_value
                                total1=total1+player_value1

                        else:
                            player_value=int(list_of_cards[b])
                            if total1>0:
                                total=total+player_value
                                total1=total1+player_value
                            elif total1==0:
                                total=total+player_value
                        list_of_cards.remove(list_of_cards[b])
                        if total1>21 and total<21 and total!=0:
                            total1=0
                        if total>21 and total1<21 and total1!=0:
                            total=0

                        if total1!=0 and total1!=total:
                            print("Your total is",total,"or",total1)
                        else:
                            print("Your total is",total)
                        if total==21 or total1==21:
                            print("Congratulations, you got Blackjack!")
                            m=m+(w*3)/2
                            player_wins=player_wins+1
                            break
                        if total>21 or total1>21:
                            print("You went bust")
                            m=m-w
                            computer_wins=computer_wins+1
                            break
                        a=input("Would you like to hit or to stay? Type h to hit and s to stay:")
                    while a=='s':
                            print("Now it is the computer's turn")
                            'computer turn'
                            value_to_beat=max(total,total1)
                            b=randint(0,len(list_of_cards)-1)
                            print('The computer drew a',list_of_cards[b])
                            if list_of_cards[b]=="K" or list_of_cards[b]=="Q" or list_of_cards[b]=="J":
                                player_value=10
                                if ctotal1>0:
                                    ctotal=ctotal+player_value
                                    ctotal1=ctotal1+player_value
                                elif ctotal1==0:
                                    ctotal=ctotal+player_value
                            elif list_of_cards[b]=="A":
                                player_value=1
                                player_value1=11
                                ctotal=ctotal+player_value
                                ctotal1=ctotal1+player_value1

                            else:
                                player_value=int(list_of_cards[b])
                                if ctotal1>0:
                                    ctotal=ctotal+player_value
                                    ctotal1=ctotal1+player_value
                                elif ctotal1==0:
                                    ctotal=ctotal+player_value
                            list_of_cards.remove(list_of_cards[b])
                            if ctotal1>21 and ctotal<21 and ctotal!=0:
                                ctotal1=0
                            if ctotal>21 and ctotal1<21 and ctotal1!=0:
                                ctotal=0
                            if ctotal1!=0 and ctotal1!=ctotal:
                                print("The computer total is",ctotal,"or",ctotal1)
                            else:
                                print("The computer total is",ctotal)
                            if ctotal>21 or ctotal1>21:
                                print("The computer went bust")
                                m=m+w
                                player_wins=player_wins+1
                                break
                            if ctotal==21 or ctotal1==21:
                                print("The computer got blackjack")
                                m=m-w
                                computer_wins=computer_wins+1
                                break
                            if ctotal1==0:
                                ctotal1=ctotal
                            while ctotal<value_to_beat and ctotal1<value_to_beat:
                                b=randint(0,len(list_of_cards)-1)
                                print('The computer drew a',list_of_cards[b])
                                if list_of_cards[b]=="K" or list_of_cards[b]=="Q" or list_of_cards[b]=="J":
                                    player_value=10
                                    if ctotal1>0:
                                        ctotal=ctotal+player_value
                                        ctotal1=ctotal1+player_value
                                    elif ctotal1==0:
                                        ctotal=ctotal+player_value
                                elif list_of_cards[b]=="A":
                                    player_value=1
                                    player_value1=11
                                    ctotal=ctotal+player_value
                                    ctotal1=ctotal1+player_value1

                                else:
                                    player_value=int(list_of_cards[b])
                                    if ctotal1>0:
                                        ctotal=ctotal+player_value
                                        ctotal1=ctotal1+player_value
                                    elif ctotal1==0:
                                        ctotal=ctotal+player_value
                                list_of_cards.remove(list_of_cards[b])
                                if ctotal1>21 and ctotal<21 and ctotal!=0:
                                    ctotal1=0
                                if ctotal>21 and ctotal1<21 and ctotal1!=0:
                                    ctotal=0
                                if ctotal1!=0 and ctotal1!=ctotal:
                                    print("The computer total is",ctotal,"or",ctotal1)
                                else:
                                    print("The computer total is",ctotal)
                                if ctotal>21 or ctotal1>21:
                                    print("The computer went bust")
                                    m=m+w
                                    player_wins=player_wins+1
                                    break
                                if ctotal==21 or ctotal1==21:
                                    print("The computer got blackjack")
                                    m=m-w
                                    computer_wins=computer_wins+1
                                    break
                                if ctotal1==0:
                                    ctotal1=ctotal
                            if (ctotal>value_to_beat and ctotal<21) or (ctotal1>value_to_beat and ctotal1<21):
                                print("The computer won")
                                m=m-w
                                computer_wins=computer_wins+1
                            if ctotal==value_to_beat or ctotal1==value_to_beat:
                                print("The game was a draw")
                                m=m
                                draws += 1
                            break
                    break
        while m<=0:
            print("Game over. You lost all of the money :(")
            break
blackjack()

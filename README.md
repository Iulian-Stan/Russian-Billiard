# Russian Billiard

This is a 2D implementation of the **Russian pyramid** game with simplistic physics.

## Rules

The player has control other a single ball, used to pocket (pot) remaining balls. 
Fist player to pot most of the balls wins. 

## Functionality

The application supports both single and multi-player modes. In the first case the same user 
plays for both sides. In the second scenario two instances of the application interact as
server and client. As long a player on one side makes a move, the inerface on the other one
is blocked. The commuication is done through datagramms (UDP) by sending all balls coordinates.

## Interface 

comming soon

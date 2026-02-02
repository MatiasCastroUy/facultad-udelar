all: programa

programa: ./include/*.h ./src/*.cpp
	g++ ./src/*.cpp -o programa

valgrind: programa
	valgrind --leak-check=full --track-origins=yes ./programa
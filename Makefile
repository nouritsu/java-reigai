build: ./src/*.java
	javac ./src/*.java -d "./bin/"

run: build
	java -cp './bin' 'Reigai' $(file)
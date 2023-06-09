ifeq (run,$(firstword $(MAKECMDGOALS)))
  # use the rest as arguments for "run"
  RUN_ARGS := $(wordlist 2,$(words $(MAKECMDGOALS)),$(MAKECMDGOALS))
  # ...and turn them into do-nothing targets
  $(eval $(RUN_ARGS):;@:)
endif

build: ./src/*.java clean
	javac ./src/*.java -d "./bin/"

run: build
	java -cp './bin' 'Reigai' $(RUN_ARGS)

ast:
	java ".\helper\GenerateAST.java" ".\src"

clean: 
	del ".\bin\*.class"

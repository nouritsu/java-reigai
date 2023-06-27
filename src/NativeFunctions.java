import java.util.List;

class NativeFunctions {
    static ReigaiCallable Clock = new ReigaiCallable() {
        @Override
        public int arity() {
            return 0;
        }

        @Override
        public Object call(Interpreter interpreter, List<Object> arguments) {
            return (double) System.currentTimeMillis() / 1000.0;
        }

        @Override
        public String toString() {
            return "<native fun>";
        }
    };

    static ReigaiCallable Len = new ReigaiCallable() {
        @Override
        public int arity() {
            return 1;
        }

        @Override
        public Object call(Interpreter interpreter, List<Object> arguments) {
            if (arguments.get(0) instanceof Double) {
                return null;
            }
            return arguments.get(0).toString().length();
        }

        @Override
        public String toString() {
            return "<native fun>";
        }
    };

    static ReigaiCallable Round = new ReigaiCallable() {
        @Override
        public int arity() {
            return 1;
        }

        @Override
        public Object call(Interpreter interpreter, List<Object> arguments) {
            if (arguments.get(0) instanceof Double) {
                return Math.round((Double) arguments.get(0));
            }
            return null;
        }

        @Override
        public String toString() {
            return "<native fun>";
        }
    };

    static ReigaiCallable Abs = new ReigaiCallable() {
        @Override
        public int arity() {
            return 1;
        }

        @Override
        public Object call(Interpreter interpreter, List<Object> arguments) {
            if (arguments.get(0) instanceof Double) {
                return Math.abs((Double) arguments.get(0));
            }
            return null;
        }

        @Override
        public String toString() {
            return "<native fun>";
        }
    };

    static ReigaiCallable Floor = new ReigaiCallable() {
        @Override
        public int arity() {
            return 1;
        }

        @Override
        public Object call(Interpreter interpreter, List<Object> arguments) {
            if (arguments.get(0) instanceof Double) {
                return Math.floor((Double) arguments.get(0));
            }
            return null;
        }

        @Override
        public String toString() {
            return "<native fun>";
        }
    };

    static ReigaiCallable Ceil = new ReigaiCallable() {
        @Override
        public int arity() {
            return 1;
        }

        @Override
        public Object call(Interpreter interpreter, List<Object> arguments) {
            if (arguments.get(0) instanceof Double) {
                return Math.ceil((Double) arguments.get(0));
            }
            return null;
        }

        @Override
        public String toString() {
            return "<native fun>";
        }
    };

    static ReigaiCallable Pow = new ReigaiCallable() {
        @Override
        public int arity() {
            return 2;
        }

        @Override
        public Object call(Interpreter interpreter, List<Object> arguments) {
            if (arguments.get(0) instanceof Double && arguments.get(1) instanceof Double) {
                return Math.pow((Double) arguments.get(0), (Double) arguments.get(1));
            }
            return null;
        }

        @Override
        public String toString() {
            return "<native fun>";
        }
    };
}
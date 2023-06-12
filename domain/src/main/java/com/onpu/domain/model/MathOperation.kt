package com.onpu.domain.model

enum class MathOperation {
    ADDITION {
        override fun performOperation(num1: Int, num2: Int): Int {
            return num1 + num2
        }
    },
    SUBTRACTION {
        override fun performOperation(num1: Int, num2: Int): Int {
            return num1 - num2
        }
    },
    MULTIPLICATION {
        override fun performOperation(num1: Int, num2: Int): Int {
            return num1 * num2
        }
    },
    DIVISION {
        override fun performOperation(num1: Int, num2: Int): Int {
            if (num2 != 0) {
                return num1 / num2
            } else {
                throw IllegalArgumentException("Division by zero is not allowed")
            }
        }
    };

    abstract fun performOperation(num1: Int, num2: Int): Int
}

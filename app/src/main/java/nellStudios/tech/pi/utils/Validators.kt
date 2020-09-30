package nellStudios.tech.pi.utils

class Validators {
    companion object {
        fun isValid(number: String): Boolean = number.trim().length == 10
    }
}
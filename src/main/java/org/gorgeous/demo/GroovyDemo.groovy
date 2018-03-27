package org.gorgeous.demo

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

/**
 * Groovy demo code
 *
 * @Author L.Yang
 */
@CompileStatic
@Slf4j
class GroovyDemo {
    static void main(String[] args) {
        0.upto(5, { print "$it " })

        String str = "123"
        int tmpNumber = str.toInteger()
        int val = tmpNumber + 2
        println("hello groovy")
        log.info("hello groovy")
        log.debug("test log print {}", val)

        [1, 2, 3, 4].each { it -> print(it * 2 + " ") }
        print(" " + 1 + 2)
        display([2, 3, 4])
        println(plusNumber(4, 5))
        def flag = "true"
        println(flag.getClass())
        boolean bFlag = flag.toBoolean()
        println(bFlag.getClass())
        4.times { print " $it" }


    }

    private static display(List<?> data) {
        data.each { print("$it ") }
    }

    private static def plusNumber(Integer a, Integer b) {
        return a + b
    }
}


class FactoryDemo {
    static void main(String[] args) {
        GameFactory.factory =
                [messages: GuessGameMessages, control: GuessGameControl, converter: GuessGameInputConverter]
//        [messages: TwoupMessages, control: TwoupControl, converter: TwoupInputConverter]
        def messages = GameFactory.messages
        def control = GameFactory.control
        def converter = GameFactory.converter
        println messages.welcome
        def reader = new BufferedReader(new InputStreamReader(System.in))
        while (control.moreTurns()) {
            def input = reader.readLine().trim()
            control.play(converter.convert(input))
        }
        println messages.done
    }
}

interface Message {
    def getWelcome()

    def getDone()
}

interface Control {
    def moreTurns()

    def play(amount)
}

trait Converter {
    def convert(input) {
        input.toInteger()
    }
}

class TwoupMessages implements Message {
    def welcome = 'Welcome to the twoup game, you start with $1000'
    def done = 'Sorry, you have no money left, goodbye'
}

class TwoupInputConverter implements Converter {
}

class TwoupControl implements Control {
    private money = 1000
    private random = new Random()

    private tossWasHead() {
        def next = random.nextInt()
        return next % 2 == 0
    }

    def moreTurns() {
        if (money > 0) {
            println "You have $money, how much would you like to bet?"
            return true
        }

        false
    }

    def play(amount) {
        def coin1 = tossWasHead()
        def coin2 = tossWasHead()
        if (coin1 && coin2) {
            money += amount
            println 'You win'
        } else if (!coin1 && !coin2) {
            money -= amount
            println 'You lose'
        } else {
            println 'Draw'
        }
    }
}

class GuessGameMessages implements Message {
    def welcome = 'Welcome to the guessing game, my secret number is between 1 and 100'
    def done = 'Correct'
}

class GuessGameInputConverter implements Converter {
}

class GuessGameControl implements Control {
    private lower = 1
    private upper = 100
    private guess = new Random().nextInt(upper - lower) + lower

    def moreTurns() {
        def done = (lower == guess || upper == guess)
        if (!done) {
            println "Enter a number between $lower and $upper"
        }

        !done
    }

    def play(nextGuess) {
        if (nextGuess <= guess) {
            lower = [lower, nextGuess].max()
        }
        if (nextGuess >= guess) {
            upper = [upper, nextGuess].min()
        }
    }
}

class GameFactory {

    def static factory

    static Message getMessages() { return factory.messages.newInstance() }

    static Control getControl() { return factory.control.newInstance() }

    static Converter getConverter() { return factory.converter.newInstance() }
}

require: slotfilling/slotFilling.sc
    module = sys.zb-common
  
require: text/text.sc
    module = sys.zb-common

require: responseCapitals.js

require: common.js
    module = sys.zb-common
    
# Для игры Назови столицу    
require: where/where.sc
    module = sys.zb-common


# Для игры Виселица
require: hangmanGameData.csv
    name = HangmanGameData
    var = $HangmanGameData

patterns:
    $Word = $entity<HangmanGameData> || converter = function ($parseTree) {
        var id = $parseTree.HangmanGameData[0].value;
        return $HangmanGameData[id].value;
        };

theme: /

    state: StartGame
        q!: $regex</start>
        intent!: /StartGame
        a: Давай поиграем в "Назови столицу". Я говорю тебе страну, а ты называешь столицу. Начнем?
        go!: Согласен?


        state: Yes
            intent: /Acceptance
            script: 
                $session.keys = Object.keys($Capital)
                $session.total = 0
                $session.right_answers = 0
            go!: /Game

        state: No
            intent: /Decline
            a: Ну и ладно! Если передумаешь — скажи "давай поиграем"
        
    state: Game
        script:
            $session.key = chooseRandCapitalKey($session.keys)
            var country = $caila.inflect($Capital[$session.key].value.country, ["loc2"])
            $reactions.answer("Назови столицу " + country);
            
            

    state: CityPattern
        q: * $Capital *
        script: 
            $session.total++
            if (checkCapital($Capital, $session.key, $parseTree._Capital.name)){
                $session.right_answers++
                $session.keys.splice(session.key)
                $reactions.answer("Правильно!");
                
                }
            else{
                
                $reactions.answer("Ты ошибся!");
            }
            $session.keys.splice(session.key)
            $reactions.transition("/Game");
        
    state: StopGame
        intent!: /StopGame
        a: Хорошо, спасибо за игру! Количество правильных ответов - {{$session.right_answers}} из {{$session.total}}

    state: NoMatch
        event!: noMatch
        a: Пожалуйста назовите столицу!

    state: reset
        q!: reset
        script:
            $session = {};
            $client = {};
        go!: /Start


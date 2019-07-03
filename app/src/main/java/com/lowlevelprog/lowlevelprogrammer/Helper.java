package com.lowlevelprog.lowlevelprogrammer;

import java.util.Arrays;

class Helper {

    // 2d arrays for saving questions with their respective choices.
    // [x][0] is always a question
    // [x][y] where y > 0 && y <= 4 are the choices
    // 4 sets for each part
    private String[][] question_set_1 = {
            {"Тезис Чёрча (1936): Каждая эффективно вычислимая функция является", "мю-рекурсивной",
                    "ля-рекурсивной", "ре-рекурсивной", "не рекурсивной"},
            {"Множество сообщаемых нам время от времени правил, которые точно регламентируют наше" +
                    " поведение",
                    "энергия космоса", "эффективная функция", "эффективная процедура", "интуиция"},
            {"Класс абстрактных машин, определенный Тьюрингом", "машина времени", "машина Тьюринга",
                    "Macintosh", "System 360"},
            {"Универсальная Машина Тьюринга (МТ) может заменить",
                    "2 МТ", "10 МТ", "любую МТ", "не любую МТ"},
            {"Проблема останова была решена в",
                    "1950", "1976", "2016", "не была решена"}
    };
    private int[] answer_set_1 = {1, 3, 2, 3, 4};

    private String[][] question_set_2 = {
            {"Модель абстрактного вычислителя Алана Тьюринга была предложена в", "1936",
                    "1937", "1940", "1935"},
            {"Назовите число принципов фон Неймона", "5", "4", "6", "3"},
            {"Назовите число основных регистров в EDSAC", "4", "5", "6", "3"},
            {"EDSAC был основан на ?-битном слове", "16", "28", "18", "26"},
            {"EDSAC был основан в", "1950", "1948", "1945", "1949"}
    };
    private int[] answer_set_2 = {1, 1, 2, 3, 4};

    private String[][] question_set_3 = {
            {"Машина Тьюринга была сконструирована в", "1940",
                    "1936", "не была", "1937"},
            {"Принципы фон Неймана были опубликованы в", "1946", "1944", "1940", "1948"},
            {"Сколько битов использовалось в EDSAC?", "18", "17", "26", "25"},
            {"Третье поколение ЭВМ было с 1965 по", "1975", "1985", "1990", "1980"}
    };
    private int[] answer_set_3 = {3, 1, 2, 4};

    private String[][] question_set_4 = {
            {"Детерминированный конечный автомат состоит из ? элементов", "2",
                    "4", "5", "3"},
            {"Вместе с фон Нейманом и Голдстайном в описании принципов построения ЭВМ участвовал",
                    "Нойс", "Килби", "Шокли", "Беркс"},
    };
    private int[] answer_set_4 = {3, 4};

    // adding bitcoins according to the difficulty of the question
    int calculateScore(int index, int currentScore) {
        switch (index) {
            case 0:
                return currentScore + 1000;
            case 1:
                return currentScore + 2000;
            case 2:
                return currentScore + 5000;
            default:
                return currentScore + 10000;
        }
    }

    // is the given answer a correct one?
    public Boolean checkAnswer(int index, String answer, int setNumber) {
        switch (setNumber) {
            case 0:
                return question_set_1[index][answer_set_1[index]].equals(answer);
            case 1:
                return question_set_2[index][answer_set_2[index]].equals(answer);
            case 2:
                return question_set_3[index][answer_set_3[index]].equals(answer);
            default:
                return question_set_4[index][answer_set_4[index]].equals(answer);
        }
    }

    // Унать номер кнопки, на которйо написан ответ на вопрос
    public int getAnswerIndex(int index, int setNumber){
        switch (setNumber) {
            case 0:
                return answer_set_1[index] - 1;
            case 1:
                return answer_set_2[index] - 1;
            case 2:
                return answer_set_3[index] - 1;
            default:
                return answer_set_4[index] - 1;
        }
    }

    // retrieve a question by its index and set number
    public String getQuestion(int index, int setNumber) {
        switch (setNumber) {
            case 0:
                return question_set_1[index][0];
            case 1:
                return question_set_2[index][0];
            case 2:
                return question_set_3[index][0];
            default:
                return question_set_4[index][0];
        }
    }

    // retrieve all the choices for a certain question
    public String[] getChoices(int index, int setNumber) {
        switch (setNumber) {
            case 0:
                return Arrays.copyOfRange(question_set_1[index], 1, question_set_1[index].length);
            case 1:
                return Arrays.copyOfRange(question_set_2[index], 1, question_set_2[index].length);
            case 2:
                return Arrays.copyOfRange(question_set_3[index], 1, question_set_3[index].length);
            default:
                return Arrays.copyOfRange(question_set_4[index], 1, question_set_4[index].length);
        }
    }

    // number of questions in the game
    public int count() {
        return 10;
    }
}


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
                    "1950", "1976", "2016", "не была решена"},
            {"Человек, создавший первый в истории компьютерный вирус", "Скрент",
                    "Попов", "Джобс", "Коренев"},
            {"Когда началась история вычислительной техники", "1938 с Цузе", "1949 с Неймоном",
                    "1954", "3000 до н.э."},
            {"Как назывались первые счеты", "дурак", "ломак", "алмак", "абак"},
            {"Первым в истории программистом была", "Лада", "Помада", "Ада", "Нивада"}
    };
    private int[] answer_set_1 = {1, 3, 2, 3, 4, 1, 4, 4, 3};

    private String[][] question_set_1_hard = {
            {"Тезис Чёрча (1936): Каждая эффективно вычислимая функция является ...-рекурсивной",
                    "мю"},
            {"Множество сообщаемых нам время от времени правил, которые точно регламентируют наше" +
                    " поведение",
                    "эффективная процедура"},
            {"Класс абстрактных машин, определенный Тьюрингом", "машина Тьюринга"},
            {"Универсальная Машина Тьюринга (МТ) может заменить... (сколько МТ)\n" +
                    "(Если точного численного ответа нет, поставьте '-1'", "-1"},
            {"Проблема останова была решена в\n" +
                    "Если точного численного ответ нет, поставьте '-1'", "-1"}
    };

    private String[][] question_set_2 = {
            {"Модель абстрактного вычислителя Алана Тьюринга была предложена в", "1936",
                    "1937", "1940", "1935"},
            {"Назовите число принципов фон Неймона", "5", "4", "6", "3"},
            {"Назовите число основных регистров в EDSAC", "4", "5", "6", "3"},
            {"EDSAC был основан на ?-битном слове", "16", "28", "18", "26"},
            {"EDSAC был основан в", "1950", "1948", "1945", "1949"},
            {"Как в языке С обозначается 'правда'", "1", "pravda", "true", "0"},
            {"Первый байт в памяти соответствует наименее значащим битам регистра", "big-endian",
                    "big-byte", "little-byte", "little-endian"},
            {"Первый байт в памяти соответствует наиболее значащим битам регистра", "big-endian",
                    "big-byte", "little-byte", "little-endian"},
            {"Кто изобрел первую вычислительную механическую машину", "Паскаль", "Ньютон",
                    "Леонардо да Винчи", "Архимед"},
            {"Кто является автором технологии WWW", "Билл Гейтс", "Энгельбарт", "Тим Бернерс-Ли",
                    "Холлерит"},
            {"Технология создания корпоративных информационных систем, основанная на протоколах" +
                    " интернета, но без доступа в глобальную сеть", "Intranet", "Etranet",
                    "Internet", "FTP"}
    };
    private int[] answer_set_2 = {1, 1, 2, 3, 4, 1, 4, 1, 1, 3, 1};

    private String[][] question_set_2_hard = {
            {"Модель абстрактного вычислителя Алана Тьюринга была предложена в", "1936"},
            {"Назовите число принципов фон Неймона", "5"},
            {"Назовите число основных регистров в EDSAC", "5"},
            {"EDSAC был основан на ?-битном слове", "18"},
            {"EDSAC был основан в", "1949"}
    };

    private String[][] question_set_3 = {
            {"Машина Тьюринга была сконструирована в", "1940",
                    "1936", "не была", "1937"},
            {"Принципы фон Неймана были опубликованы в", "1946", "1944", "1940", "1948"},
            {"Сколько битов использовалось в EDSAC?", "18", "17", "26", "25"},
            {"Третье поколение ЭВМ было с 1965 по", "1975", "1985", "1990", "1980"},
            {"Как называлось запоминающее устройство в EDSAC", "трубка Уильямса", "трубка Неймана",
                    "Трубка Цузе", "трубка Голдстайна"},
            {"Первый американский программируемый компьютер", "Mark 1", "ENIAC", "EDVAC", "IBM 1"},
            {"Первый американский программируемый компьютер был построен в", "1940", "1945",
                    "1941", "1946"},
            {"Первым в мире компьютером с графическим пользовательским интерфейсом был", "Apple I",
                    "Xerox Alto", "Apple IIc Plus", "IBM 5100"},
            {"Как звали прародителя современных ноутбуков", "Macintosh PowerBook", "PDP-1",
                    "Hewlett-Packard 980A", "GRiD Compass 1101"}
    };
    private int[] answer_set_3 = {3, 1, 2, 4, 1, 1, 3, 2, 3};

    private String[][] question_set_3_hard = {
            {"Машина Тьюринга была сконструирована в ...\n" +
                    "Если точного численного ответа нет, поставьте '-1'", "-1"},
            {"Принципы фон Неймана были опубликованы в", "1946"},
            {"Сколько битов использовалось в EDSAC?", "17"},
            {"Третье поколение ЭВМ было с 1965 по", "1980"}
    };

    private String[][] question_set_4 = {
            {"Детерминированный конечный автомат состоит из ? элементов", "2",
                    "4", "5", "3"},
            {"Вместе с фон Нейманом и Голдстайном в описании принципов построения ЭВМ участвовал",
                    "Нойс", "Килби", "Шокли", "Беркс"},
            {"Как нызвается промежуточный бит в длинных словах в EDSAC", "unite", "sandwich",
                    "glue", "plasticine"},
            {"Советская ЭВМ, первая в СССР и континентальной Европе", "ММСЭ", "МЭСМ", "ЭСММ",
                    "МСЭМ"},
            {"Как назывался самый тяжелый настольный компьютер в истории", "Apple II", "IBM 5120",
                    "MITS Altair", "Xerox 820"},
            {"Этот компьютер из «Терминатор 2: Судный день», где он используется молодым Джоном " +
                    "Коннором, чтобы обойти систему безопасности банкомата",
                    "Atari Portfolio", "Hewlett-Packard 9825", "Victor 9000", "Tano Dragon"},
            {"Первым в истории человечества компьютерным вирусом стал", "Trojan", "WANNA CRY",
                    "ELK CLONER", "Virus"},
            {"Кто изобрел компьютерную мышь", "Бэббидж", "Энгельбарт", "Холлерит", "Атанасов"}
    };
    private int[] answer_set_4 = {3, 4, 2, 2, 2, 1, 3, 2};

    private String[][] question_set_4_hard = {
            {"Детерминированный конечный автомат состоит из ? элементов", "5"},
            {"Вместе с фон Нейманом и Голдстайном в описании принципов построения ЭВМ участвовал",
                    "Беркс"},
    };

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

    // adding bitcoins in the hard mode
    // 70 percent for approximate answer
    int calculateScoreHard(int index, String answer, int setNumber, int currentScore) {
        String correctAnswer;
        switch (setNumber) {
            case 0:
                correctAnswer = question_set_1_hard[index][1];
                if (index <= 2) {
                    return currentScore + 1000;
                } else if (Math.abs(Integer.parseInt(answer) -
                        Integer.parseInt(correctAnswer)) == 0) {
                    return currentScore + 1000;
                } else {
                    return currentScore + 700;
                }
            case 1:
                correctAnswer = question_set_2_hard[index][1];
                if (Math.abs(Integer.parseInt(answer) -
                        Integer.parseInt(correctAnswer)) == 0) {
                    return currentScore + 2000;
                } else {
                    return currentScore + 1400;
                }
            case 2:
                correctAnswer = question_set_3_hard[index][1];
                if (Math.abs(Integer.parseInt(answer) -
                        Integer.parseInt(correctAnswer)) == 0) {
                    return currentScore + 5000;
                } else {
                    return currentScore + 3500;
                }
            default:
                correctAnswer = question_set_4_hard[index][1];
                if (index == 1) {
                    return currentScore + 10000;
                } else if (Math.abs(Integer.parseInt(answer) -
                        Integer.parseInt(correctAnswer)) == 0) {
                    return currentScore + 10000;
                } else {
                    return currentScore + 7000;
                }
        }
    }

    // is the given answer a correct one?
    Boolean checkAnswer(int index, String answer, int setNumber) {
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

    // checking the answer with approximation (hard mode)
    // when the user must type a numerable in, there can be an error
    // error`s value is up to 3
    // the score that a user will get for the exact answer and for the approximate
    // answer is different
    Boolean checkAnswerHard(int index, String answer, int setNumber) {
        String correctAnswer;
        switch (setNumber) {
            case 0:
                correctAnswer = question_set_1_hard[index][1];
                if (index <= 2) {
                    return correctAnswer.equalsIgnoreCase(answer);
                } else {
                    if (Integer.parseInt(correctAnswer) != -1) {
                        return (Math.abs(Integer.parseInt(answer) -
                                Integer.parseInt(correctAnswer)) <= 1);
                    } else return
                            correctAnswer.equalsIgnoreCase(answer);
                }
            case 1:
                correctAnswer = question_set_2_hard[index][1];
                return (Math.abs(Integer.parseInt(answer) -
                        Integer.parseInt(correctAnswer)) <= 1);
            case 2:
                correctAnswer = question_set_3_hard[index][1];
                return (Math.abs(Integer.parseInt(answer) -
                        Integer.parseInt(correctAnswer)) <= 1);
            default:
                correctAnswer = question_set_4_hard[index][1];
                if (index == 1) return correctAnswer.equalsIgnoreCase(answer);
                else return (Math.abs(Integer.parseInt(answer) -
                        Integer.parseInt(correctAnswer)) <= 1);
        }
    }

    // Унать номер кнопки, на которйо написан ответ на вопрос
    int getAnswerIndex(int index, int setNumber) {
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
    String getQuestion(int index, int setNumber) {
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

    // retrieve a question for the hard mode
    String getHardQuestion(int index, int setNumber) {
        switch (setNumber) {
            case 0:
                return question_set_1_hard[index][0];
            case 1:
                return question_set_2_hard[index][0];
            case 2:
                return question_set_3_hard[index][0];
            default:
                return question_set_4_hard[index][0];
        }
    }

    // retrieve all the choices for a certain question
    String[] getChoices(int index, int setNumber) {
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
    int count() {
        return 10;
    }
}


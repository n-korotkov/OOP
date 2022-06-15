package ru.n_korotkov.oop.dsl

tasks = [
    {
        id = "Task_2_1_1"
        name = "Prime numbers"
        score = 1
        runTests = true
    },
    {
        id = "Task_2_2_1"
        name = "Pizzeria"
        score = 2
        runTests = true
    },
    {
        id = "Task_2_3_1"
        name = "Snake"
        score = 3
        runTests = false
    },
    {
        id = "BuildFailingTask"
        name = "BuildFailingTask"
        score = 1
        runTests = false
    },
    {
        id = "TestFailingTask"
        name = "TestFailingTask"
        score = 1
        runTests = true
    }
]

groupName = "20213"
students = [
    {
        id = "n-korotkov"
        name = "Nikita Korotkov"
        repoURL = "https://github.com/n-korotkov/OOP.git"
        branch = "Task_2_2_1"
    },
    {
        id = "leadpogrommer"
        name = "Ilya Merzlyakov"
        repoURL = "https://github.com/leadpogrommer/OOP.git"
        branch = "master"
    }
]

assignments = [
    {
        studentId = "leadpogrommer"
        taskId = "Task_2_3_1"
    },
    {
        studentId = "leadpogrommer"
        taskId = "BuildFailingTask"
    },
    {
        studentId = "leadpogrommer"
        taskId = "TestFailingTask"
    },
    {
        studentId = "n-korotkov"
        taskId = "Task_2_1_1"
    },
    {
        studentId = "n-korotkov"
        taskId = "Task_2_2_1"
    }
]

repositoriesDirectory = "./repositories/"

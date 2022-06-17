package ru.n_korotkov.oop.dsl

tasks = [
    {
        id = "Task_1_1_1"
        name = "Heap sort"
        score = 1
        runTests = true
    },
    {
        id = "Task_1_1_2"
        name = "String search"
        score = 1
        runTests = true
    },
    {
        id = "Task_1_3_1"
        name = "Heap sort"
        score = 1
        runTests = true
    },
    {
        id = "Task_1_1_1"
        name = "Heap sort"
        score = 1
        runTests = true
    },
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
        name = "Build failing task"
        score = 1
        runTests = false
    },
    {
        id = "TestFailingTask"
        name = "Test failing task"
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
    },
    {
        id = "leadpogrommer"
        name = "Ilya Merzlyakov"
        repoURL = "https://github.com/leadpogrommer/OOP.git"
    },
    {
        id = "alinaguselnikova"
        name = "Alina Guselnikova"
        repoURL = "https://github.com/alinaguselnikova/OOP.git"
    }
]

assignments = [
    {
        studentId = "leadpogrommer"
        taskId = "Task_1_1_1"
    },
    {
        studentId = "leadpogrommer"
        taskId = "Task_1_1_2"
    },
    {
        studentId = "leadpogrommer"
        taskId = "Task_2_1_1"
    },
    {
        studentId = "leadpogrommer"
        taskId = "Task_2_2_1"
        branch = "doesNotExist"
    },
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
        taskId = "Task_1_1_1"
        branch = "main"
    },
    {
        studentId = "n-korotkov"
        taskId = "Task_1_1_2"
        branch = "main"
    },
    {
        studentId = "n-korotkov"
        taskId = "Task_2_1_1"
        branch = "Task_2_2_1"
    },
    {
        studentId = "n-korotkov"
        taskId = "Task_2_2_1"
        branch = "Task_2_2_1"
    },
    {
        studentId = "alinaguselnikova"
        taskId = "Task_1_1_1"
    },
    {
        studentId = "alinaguselnikova"
        taskId = "Task_2_3_1"
    }
]

repositoriesDirectory = "./repositories/"

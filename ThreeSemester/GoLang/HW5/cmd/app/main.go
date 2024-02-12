package main

import (
	m "hw3/internal/mark"
	"log"
	"os"
)

func main() {
	file, err := os.Open("./data/input_1.tsv")
	if err != nil {
		log.Fatal("error with reading input file: ", err)
	}
	defer file.Close()
	studentsStatistic, err := m.ReadStudentsStatistic(file)
	if err != nil {
		log.Fatal("error with reading input file: ", err)
	}
	file, err = os.Create("./data/output_1.tsv")
	if err != nil {
		log.Fatal("error with reading output file: ", err)
	}
	defer file.Close()
	err = m.WriteStudentsStatistic(file, studentsStatistic)
	if err != nil {
		log.Fatal("error with writing output file: ", err)
	}
}

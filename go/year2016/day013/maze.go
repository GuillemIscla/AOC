package main

import (
	"fmt"
	"math/bits"
	"strconv"
	"strings"
)

type Maze struct {
	maxX           int
	maxY           int
	favoriteNumber int
	cachedValues   map[[2]int]rune
}

func NewMaze(favoriteNumber int) Maze {
	return Maze{
		favoriteNumber: favoriteNumber,
		cachedValues:   make(map[[2]int]rune),
	}
}

func (m *Maze) Explore(x int, y int) rune {
	result := m.cachedValues[[2]int{x, y}]
	m.maxX = max(m.maxX, x+1)
	m.maxY = max(m.maxY, y+1)

	if result == 0 {
		formula := x*x + 3*x + 2*x*y + y + y*y + m.favoriteNumber
		bitsCount := bits.OnesCount(uint(formula))
		if bitsCount%2 == 0 {
			return '.'
		} else {
			return '#'
		}
	}
	return result
}

func (m Maze) String() string {
	var result strings.Builder
	result.WriteString(" ")
	for i := 0; i < m.maxX; i++ {
		result.WriteString(strconv.Itoa((i % 10)))
	}
	result.WriteString("\n")
	for j := 0; j < m.maxY; j++ {
		result.WriteString(strconv.Itoa((j % 10)))
		for i := 0; i < m.maxX; i++ {
			fmt.Fprintf(&result, "%c", m.Explore(i, j))
		}
		result.WriteString("\n")
	}
	return result.String()
}

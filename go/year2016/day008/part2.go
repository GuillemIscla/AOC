package main

import "fmt"

func part2() string {
	instructions, _ := parseInput(nil)

	var screen [50][6]bool

	for _, instruction := range instructions {
		instruction.Process(&screen)
	}

	// // Print the screen to know which characters to code
	// for y := 0; y < 6; y++ {
	// 	for x := 0; x < 50; x++ {
	// 		if screen[x][y] {
	// 			fmt.Print("█")
	// 		} else {
	// 			fmt.Print(" ")
	// 		}
	// 	}
	// 	fmt.Println()
	// }

	characters := ""
	var screenSection [5][6]bool

	for y := 0; y < 10; y++ {
		for i := 0; i < 5; i++ {
			for j := 0; j < 6; j++ {
				screenSection[i][j] = screen[i+y*5][j]
			}
		}
		character, _ := ReadScreen(&screenSection)
		characters += string(character)
	}
	return characters
}

func ReadScreen(screenSection *[5][6]bool) (rune, error) {
	lookup := make(map[[5][6]bool]rune)
	var f = [6][5]rune{
		{'█', '█', '█', '█', ' '},
		{'█', ' ', ' ', ' ', ' '},
		{'█', '█', '█', ' ', ' '},
		{'█', ' ', ' ', ' ', ' '},
		{'█', ' ', ' ', ' ', ' '},
		{'█', ' ', ' ', ' ', ' '},
	}

	lookup[ToCanonical(f)] = 'F'

	var h = [6][5]rune{
		{'█', ' ', ' ', '█', ' '},
		{'█', ' ', ' ', '█', ' '},
		{'█', '█', '█', '█', ' '},
		{'█', ' ', ' ', '█', ' '},
		{'█', ' ', ' ', '█', ' '},
		{'█', ' ', ' ', '█', ' '},
	}

	lookup[ToCanonical(h)] = 'H'

	var g = [6][5]rune{
		{' ', '█', '█', ' ', ' '},
		{'█', ' ', ' ', '█', ' '},
		{'█', ' ', ' ', ' ', ' '},
		{'█', ' ', '█', '█', ' '},
		{'█', ' ', ' ', '█', ' '},
		{' ', '█', '█', '█', ' '},
	}

	lookup[ToCanonical(g)] = 'G'

	var o = [6][5]rune{
		{' ', '█', '█', ' ', ' '},
		{'█', ' ', ' ', '█', ' '},
		{'█', ' ', ' ', '█', ' '},
		{'█', ' ', ' ', '█', ' '},
		{'█', ' ', ' ', '█', ' '},
		{' ', '█', '█', ' ', ' '},
	}

	lookup[ToCanonical(o)] = 'O'

	var p = [6][5]rune{
		{'█', '█', '█', ' ', ' '},
		{'█', ' ', ' ', '█', ' '},
		{'█', ' ', ' ', '█', ' '},
		{'█', '█', '█', ' ', ' '},
		{'█', ' ', ' ', ' ', ' '},
		{'█', ' ', ' ', ' ', ' '},
	}

	lookup[ToCanonical(p)] = 'P'

	var s = [6][5]rune{
		{' ', '█', '█', '█', ' '},
		{'█', ' ', ' ', ' ', ' '},
		{'█', ' ', ' ', ' ', ' '},
		{' ', '█', '█', ' ', ' '},
		{' ', ' ', ' ', '█', ' '},
		{'█', '█', '█', ' ', ' '},
	}

	lookup[ToCanonical(s)] = 'S'

	var z = [6][5]rune{
		{'█', '█', '█', '█', ' '},
		{' ', ' ', ' ', '█', ' '},
		{' ', ' ', '█', ' ', ' '},
		{' ', '█', ' ', ' ', ' '},
		{'█', ' ', ' ', ' ', ' '},
		{'█', '█', '█', '█', ' '},
	}

	lookup[ToCanonical(z)] = 'Z'

	result := lookup[*screenSection]
	if result == ' ' {
		return ' ', fmt.Errorf("cannot read character")
	}
	return result, nil
}

func ToCanonical(part_screen [6][5]rune) [5][6]bool {
	var canonical [5][6]bool
	for y := 0; y < 6; y++ {
		for x := 0; x < 5; x++ {
			canonical[x][y] = part_screen[y][x] == '█'
		}
	}
	return canonical
}

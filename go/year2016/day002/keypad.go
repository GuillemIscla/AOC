package main

type Position struct {
	X int
	Y int
}

var keypad_1 = map[Position]string{
	{X: 0, Y: 0}: "1",
	{X: 1, Y: 0}: "2",
	{X: 2, Y: 0}: "3",
	{X: 0, Y: 1}: "4",
	{X: 1, Y: 1}: "5",
	{X: 2, Y: 1}: "6",
	{X: 0, Y: 2}: "7",
	{X: 1, Y: 2}: "8",
	{X: 2, Y: 2}: "9",
}

var keypad_2 = map[Position]string{
	{X: 2, Y: 0}: "1",
	{X: 1, Y: 1}: "2",
	{X: 2, Y: 1}: "3",
	{X: 3, Y: 1}: "4",
	{X: 0, Y: 2}: "5",
	{X: 1, Y: 2}: "6",
	{X: 2, Y: 2}: "7",
	{X: 3, Y: 2}: "8",
	{X: 4, Y: 2}: "9",
	{X: 1, Y: 3}: "A",
	{X: 2, Y: 3}: "B",
	{X: 3, Y: 3}: "C",
	{X: 2, Y: 4}: "D",
}

func (p Position) Process(d Direction, keypad map[Position]string) Position {
	new_p := p
	switch {
	case d == Up:
		new_p.Y--
	case d == Down:
		new_p.Y++
	case d == Left:
		new_p.X--
	case d == Right:
		new_p.X++
	}

	_, ok := keypad[new_p]

	if ok {
		return new_p
	} else {
		return p
	}
}

package main

type Cardinal int

const (
	North Cardinal = iota
	East
	South
	West
)

type Position struct {
	X int
	Y int
}

type PositionLookAt struct {
	Position Position
	LookAt   Cardinal
}

func (p *PositionLookAt) Process(instr Instruction) {
	switch instr.Direction {
	case Left:
		p.LookAt = (p.LookAt + 3) % 4 // Turn left
	case Right:
		p.LookAt = (p.LookAt + 1) % 4 // Turn right
	}

	switch p.LookAt {
	case North:
		p.Position.Y += instr.Steps
	case South:
		p.Position.Y -= instr.Steps
	case East:
		p.Position.X += instr.Steps
	case West:
		p.Position.X -= instr.Steps
	}
}

func (p Position) DistanceFromCenter() int {
	abs := func(x int) int {
		if x < 0 {
			return -x
		}
		return x
	}
	return abs(p.X) + abs(p.Y)
}

func (p Position) BuildPath(other Position) []Position {
	var path []Position
	if p.X == other.X {
		// Vertical movement
		if p.Y < other.Y {
			for y := p.Y + 1; y <= other.Y; y++ {
				path = append(path, Position{X: p.X, Y: y})
			}
		} else {
			for y := p.Y - 1; y >= other.Y; y-- {
				path = append(path, Position{X: p.X, Y: y})
			}
		}
	} else if p.Y == other.Y {
		// Horizontal movement
		if p.X < other.X {
			for x := p.X + 1; x <= other.X; x++ {
				path = append(path, Position{X: x, Y: p.Y})
			}
		} else {
			for x := p.X - 1; x >= other.X; x-- {
				path = append(path, Position{X: x, Y: p.Y})
			}
		}
	}
	return path
}

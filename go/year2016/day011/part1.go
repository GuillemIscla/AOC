package main

func part1() int {
	// v := 1
	// state, _ := parseInput(&v)
	state, _ := parseInput(nil)

	node, _ := MinSteps(state)

	return len(node.Path) - 1
}

type Node struct {
	State State
	Path  []State
}

func MinSteps(start State) (Node, bool) {
	queue := []Node{{
		State: start,
		Path:  []State{start},
	}}

	previousStates := []State{start}

	for len(queue) > 0 {
		cur := queue[0]
		queue = queue[1:]

		if cur.State.IsFinal() {
			return cur, true
		}

		nextStates := cur.State.NextStates(previousStates)

		for _, next := range nextStates {
			nextPath := append(
				append([]State(nil), cur.Path...),
				next,
			)

			previousStates = append(previousStates, next)

			queue = append(queue, Node{
				State: next,
				Path:  nextPath,
			})
		}
	}

	return Node{}, false
}

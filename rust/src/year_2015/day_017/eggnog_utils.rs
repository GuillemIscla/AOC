use std::cmp::Ordering;

pub fn store_eggnog_count(boxes: &[usize], eggnog: usize) -> usize {
    match eggnog.cmp(&0) {
        Ordering::Equal => 1, //No more eggnog, just one combination: All the rest of the boxes
        //empty
        Ordering::Less => 0, // Not possible: eggnog is a usize, cannot be less than 0
        Ordering::Greater => match boxes.first() {
            Some(box_size) => match eggnog.checked_sub(*box_size) {
                Some(less_eggonog) => {
                    store_eggnog_count(&boxes[1..], less_eggonog) //count with this box full
                        + store_eggnog_count(&boxes[1..], eggnog) //count with this box empty
                }
                None => store_eggnog_count(&boxes[1..], eggnog), //count with this box empty
            },
            None => 0, //There is eggnog to place but no more boxes.
        },
    }
}

//Returns how many combinations + how many boxes used and we only count the possibilites with the
//minimum boxes used
pub fn store_eggnog_count_min_boxes(boxes: &[usize], eggnog: usize) -> (usize, usize) {
    match eggnog.cmp(&0) {
        //No more eggnog, just one combination: All the rest of the boxes empty
        Ordering::Equal => (1, 0),
        //Not possible since eggnog is a usize, cannot be less than 0
        Ordering::Less => (0, 0),
        //There is eggnog to place
        Ordering::Greater => match boxes.first() {
            //There is eggnog to place and (at least) one box left
            Some(box_size) => match eggnog.checked_sub(*box_size) {
                //The remaining eggnog can fit this box. We will call recursively here
                Some(less_eggonog) => {
                    //We branch the solutions with this box empty and full
                    let (solution_with_me_combinations, solution_with_me_boxes) =
                        store_eggnog_count_min_boxes(&boxes[1..], less_eggonog);
                    let (solution_without_me_combinations, solution_without_me_boxes) =
                        store_eggnog_count_min_boxes(&boxes[1..], eggnog);
                    match (
                        solution_with_me_combinations,
                        solution_without_me_combinations,
                    ) {
                        //No branch has any possibilites
                        (0, 0) => (0, 0),
                        //One branch is impossible, we pick the other
                        (_, 0) => (solution_with_me_combinations, solution_with_me_boxes + 1),
                        //One branch is impossible, we pick the other
                        (0, _) => (solution_without_me_combinations, solution_without_me_boxes),
                        //Both branch are possible, we pick the one which uses less boxes
                        _ => match (solution_with_me_boxes + 1).cmp(&solution_without_me_boxes) {
                            //Both branch are the minimum, we add up the combinations
                            Ordering::Equal => (
                                solution_with_me_combinations + solution_without_me_combinations,
                                solution_with_me_boxes + 1,
                            ),
                            //One branch uses less boxes, we pick this one
                            Ordering::Less => {
                                (solution_with_me_combinations, solution_with_me_boxes + 1)
                            }
                            //One branch uses less boxes, we pick this one
                            Ordering::Greater => {
                                (solution_without_me_combinations, solution_without_me_boxes)
                            }
                        },
                    }
                }
                //The eggnog cannot fill the box, we count possibilities with this box empty
                None => store_eggnog_count_min_boxes(&boxes[1..], eggnog),
            },
            //There is eggnog to place but no more boxes.
            None => (0, 0),
        },
    }
}

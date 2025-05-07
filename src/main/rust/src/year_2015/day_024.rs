use packages_utils::*;
use parse::*;

mod packages_utils;
mod parse;

#[allow(dead_code, unused_assignments)]
pub fn part_1() {
    let all_packages = parse(true);
    let group_weight = all_packages.iter().sum::<usize>() / 3;

    let mut front_seat_packages = 1;
    let mut quantum_entanglement: usize = 0;

    loop {
        let valid_quantum_entanglements =
            get_package_divisions(Some(front_seat_packages), group_weight, &all_packages)
                .into_iter()
                .filter(
                    //containing the first element is a huge advantage to have the lowest
                    //quantum_entanglement
                    |PackageDivision {
                         weighted_set,
                         rest: _,
                     }| {
                        weighted_set.contains(all_packages.first().unwrap())
                    },
                )
                .filter_map(|PackageDivision { weighted_set, rest }| {
                    let other_divisions = get_package_divisions(None, group_weight, &rest);
                    if other_divisions.is_empty() {
                        None
                    } else {
                        Some(get_quantum_entanglement(&weighted_set))
                    }
                })
                .collect::<Vec<usize>>();

        if let Some(result) = valid_quantum_entanglements.into_iter().min() {
            quantum_entanglement = result;
            break;
        }
        front_seat_packages += 1;
    }

    println!("Part 1: {:}", quantum_entanglement);
}

#[allow(dead_code, unused_assignments)]
pub fn part_2() {
    let all_packages = parse(true);
    let group_weight = all_packages.iter().sum::<usize>() / 4;

    let mut front_seat_packages = 1;
    let mut quantum_entanglement: usize = 0;

    loop {
        let valid_quantum_entanglements =
            get_package_divisions(Some(front_seat_packages), group_weight, &all_packages)
                .into_iter()
                .filter(
                    |PackageDivision {
                         weighted_set,
                         rest: _,
                     }| {
                        weighted_set.contains(all_packages.first().unwrap())
                    },
                )
                .filter_map(|PackageDivision { weighted_set, rest }| {
                    let other_divisions = get_package_divisions(None, group_weight, &rest);
                    if other_divisions.is_empty() {
                        None
                    } else if other_divisions.into_iter().any(
                        |PackageDivision {
                             weighted_set: _,
                             rest,
                         }| {
                            let other_other_divisions =
                                get_package_divisions(None, group_weight, &rest);
                            other_other_divisions.is_empty()
                        },
                    ) {
                        Some(get_quantum_entanglement(&weighted_set))
                    } else {
                        None
                    }
                })
                .collect::<Vec<usize>>();

        if let Some(result) = valid_quantum_entanglements.into_iter().min() {
            quantum_entanglement = result;
            break;
        }
        front_seat_packages += 1;
    }

    println!("Part 2: {}", quantum_entanglement);
}

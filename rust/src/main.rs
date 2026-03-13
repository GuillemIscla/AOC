mod year_2015;
mod year_2025;
use year_2025 as current_year;

fn main() {
    let current_day = 11;
    match current_day {
        1 => {
            current_year::day_001::part_1();
            current_year::day_001::part_2();
        }
        2 => {
            current_year::day_002::part_1();
            current_year::day_002::part_2();
        }
        3 => {
            current_year::day_003::part_1();
            current_year::day_003::part_2();
        }
        4 => {
            current_year::day_004::part_1();
            current_year::day_004::part_2();
        }
        5 => {
            current_year::day_005::part_1();
            current_year::day_005::part_2();
        }
        6 => {
            current_year::day_006::part_1();
            current_year::day_006::part_2();
        }
        7 => {
            current_year::day_007::part_1();
            current_year::day_007::part_2();
        }
        8 => {
            current_year::day_008::part_1();
            current_year::day_008::part_2();
        }
        9 => {
            current_year::day_009::part_1();
            current_year::day_009::part_2();
        }
        10 => {
            current_year::day_010::part_1();
            current_year::day_010::part_2();
        }
        11 => {
            current_year::day_011::part_1();
            current_year::day_011::part_2();
        }
        //12 => {
        //    current_year::day_011::part_1();
        //    current_year::day_011::part_2();
        //},
        //13 => {
        //    current_year::day_011::part_1();
        //    current_year::day_011::part_2();
        //},
        //14 => {
        //    current_year::day_011::part_1();
        //    current_year::day_011::part_2();
        //},
        //15 => {
        //    current_year::day_011::part_1();
        //    current_year::day_011::part_2();
        //},
        //16 => {
        //    current_year::day_011::part_1();
        //    current_year::day_011::part_2();
        //},
        //17 => {
        //    current_year::day_011::part_1();
        //    current_year::day_011::part_2();
        //},
        //18 => {
        //    current_year::day_011::part_1();
        //    current_year::day_018::part_2();
        //},
        //19 => {
        //    current_year::day_019::part_1();
        //    current_year::day_019::part_2();
        //},
        //20 => {
        //    current_year::day_020::part_1();
        //    current_year::day_020::part_2();
        //},
        //21 => {
        //    current_year::day_021::part_1();
        //    current_year::day_021::part_2();
        //},
        //22 => {
        //    current_year::day_022::part_1();
        //    current_year::day_022::part_2();
        //},
        //23 => {
        //    current_year::day_023::part_1();
        //    current_year::day_023::part_2();
        //},
        //24 => {
        //    current_year::day_024::part_1();
        //    current_year::day_024::part_2();
        //},
        //25 => {
        //    current_year::day_025::part_1();
        //    current_year::day_025::part_2();
        //},
        other => {
            println!("Day {} is wrong or not implemented yet", other);
        }
    }
}

import { Component, OnInit } from '@angular/core';
import {AchievementService} from "../../_services/achievement.service";
import {Achievement} from "../../_models/achievement";

@Component({
  selector: 'app-achievements',
  templateUrl: './achievements.component.html',
  styleUrls: ['./achievements.component.css']
})
export class AchievementsComponent implements OnInit {

  achievementList: Achievement[];


  constructor(
    private achievementService: AchievementService
  ) { }

  ngOnInit() {
    this.getAllAchievements();
  }

  getAllAchievements() {
    this.achievementService.getAllAchievementsFromService().subscribe(achievementList => {
      this.achievementList = achievementList;
      console.log(this.achievementList);
    });
  }
}

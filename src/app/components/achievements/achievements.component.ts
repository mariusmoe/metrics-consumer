import { Component, OnInit } from '@angular/core';
import {AchievementService} from "../../_services/achievement.service";
import {Achievement} from "../../_models/achievement";
import {UserAchievement} from "../../_models/user-achievement";

@Component({
  selector: 'app-achievements',
  templateUrl: './achievements.component.html',
  styleUrls: ['./achievements.component.css']
})
export class AchievementsComponent implements OnInit {

  achievementList: Achievement[];
  userAchievementList: UserAchievement[];


  constructor(
    private achievementService: AchievementService
  ) { }

  ngOnInit() {
    this.getAllAchievementData();
  }

  getUserAchievement(achievementRef: string):UserAchievement {
    // console.log(achievementRef);
    return this.userAchievementList.find(obj => {
      return obj.achievementRef === achievementRef;
    });
  }

  public getAllAchievementData() {
    this.achievementService.getAllAchievementDataFromService().subscribe(responseList => {
      console.log(responseList);
      this.achievementList = responseList[0];
      this.userAchievementList = responseList[1];
    });
  }

  getSumOfHistory(userAchievement: UserAchievement, achievement: Achievement): number{
    let sumOfPoints = 0;
    if (userAchievement && userAchievement.history) {
      Object.keys(userAchievement.history).forEach( key => {
        sumOfPoints += userAchievement.history[key];
      });
      let result = (sumOfPoints / achievement.threshold)*100;
      return result;
    } else {
      return 0.1;
    }
  }

  sumOfHistoryRaw(userAchievement: UserAchievement): number {
    let sumOfPoints = 0;
    if (userAchievement && userAchievement.history) {
      Object.keys(userAchievement.history).forEach( key => {
        sumOfPoints += userAchievement.history[key];
      });
      return sumOfPoints;
    }

    return 0;
  }
}

import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'searchFilter'
})
export class SearchFilterPipe implements PipeTransform {

  transform(items: any[], searchText: string): any[] {
    var results = [];
    if (!items) return [];
    if (!searchText) return items;

    //FIXME see if you can write a better version accessing attributes by key.
    //Also your solutions are very client side but it's important to be comfortable full stack.
    //Imagine there were 200000 users in your system. Filtering would need to be done server side
    //and your results table would have to be server side paginated.
    // obj[key]


    searchText = searchText.toLowerCase();

    var usernameResults = items.filter(it => {
      return it.username.toLowerCase().indexOf(searchText.toLowerCase().trim()) > -1;
    });

    var firstNameResults = items.filter(it => {
      return it.firstName.toLowerCase().indexOf(searchText.toLowerCase().trim()) > -1;
    });

    var lastNameResults = items.filter(it => {
      return it.lastName.toLowerCase().indexOf(searchText.toLowerCase().trim()) > -1;
    });

    var userIdResults = items.filter(it => {
      return it.userId.toLowerCase().indexOf(searchText.toLowerCase().trim()) > -1;
    });

    if (usernameResults.length) {
      for (let user of usernameResults) {
        results.push(user);
      };
    }
    if (firstNameResults.length) {
      for (let user of firstNameResults) {
        results.push(user);
      }
    }
    if (lastNameResults.length) {
      for (let user of lastNameResults) {
        results.push(user);
      }
    }
    if (userIdResults.length) {
      for (let user of userIdResults) {
        results.push(user);
      }
    }

    if (results.length === 0) {
      return [-1];
    }
    return this.removeDuplicates(results);
  }

  removeDuplicates(results) {
    return results.filter((elem, i, arr) => {
      if (arr.indexOf(elem) === i) {
        return elem
      }
    })
  }
}

import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'usernameFilter'
})
export class UsernameFilterPipe implements PipeTransform {

  transform(items: any[], searchText: string): any[] {
    if (!items) return [];
    if (!searchText) return items;
    searchText = searchText.toLowerCase();
    var results = items.filter(it => {
      return it.username.toLowerCase().indexOf(searchText.toLowerCase()) > -1;
    });
    if(results.length === 0){
      return [-1];
    }
    return results;
  }
}

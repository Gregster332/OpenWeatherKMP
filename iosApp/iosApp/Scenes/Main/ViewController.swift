import UIKit
import MultiPlatformLibrary
import MultiPlatformLibraryMvvm

class ViewController: UIViewController {

    @IBOutlet private weak var tableView: UITableView!
    @IBOutlet private weak var textField: UITextField!
    private let refreshControl = UIRefreshControl()
    
    private var viewModel: SimpleViewModel!
    private var cityArray: [RealmCityModel] = []
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        viewModel = SimpleViewModel()
        tableViewRegister()
        cityArray = viewModel.fetchAllCities()
    }
    
    private func tableViewRegister() {
        tableView.delegate = self
        tableView.dataSource = self
        tableView.register(UINib(nibName: "CityCell", bundle: nil), forCellReuseIdentifier: "CityCell")
        //tableView.refreshControl = refreshControl
        //refreshControl.addTarget(self, action: #selector(refreshWeather), for: .valueChanged)

    }
    
    @IBAction func onCounterButtonPressed() {
        viewModel.addCityToDB(name: textField.text!)
         cityArray = viewModel.fetchAllCities()
        //viewModel.onCleared()
        view.endEditing(true)
        textField.text = ""
        tableView.reloadData()
    }
    
    @IBAction func deleteAllCities() {
        viewModel.deleteAllCities()
        cityArray = viewModel.fetchAllCities()
        tableView.reloadData()
    }
    
//    @objc private func refreshWeather() {
//        viewModel.refreshWeather()
//        cityArray = viewModel.fetchAllCities()
//        refreshControl.endRefreshing()
//        tableView.reloadData()
//    }
    
    override func didMove(toParent parent: UIViewController?) {
        if(parent == nil) { viewModel.onCleared() }
    }
}


extension ViewController: UITableViewDelegate, UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        cityArray.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "CityCell", for: indexPath) as! CityCell
        cell.setCellContent(data: cityArray[indexPath.row])
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let sb = UIStoryboard(name: "ViewController", bundle: nil)
        let controller = sb.instantiateViewController(withIdentifier: "DetailViewController") as! DetailViewController
        controller.welcome = cityArray[indexPath.row]
        self.navigationController?.pushViewController(controller, animated: true)
    }
    
    func tableView(_ tableView: UITableView, canEditRowAt indexPath: IndexPath) -> Bool {
        return true
    }
    
    func tableView(_ tableView: UITableView, commit editingStyle: UITableViewCell.EditingStyle, forRowAt indexPath: IndexPath) {
        viewModel.deleteCity(name: cityArray[indexPath.row].name)
        cityArray = viewModel.fetchAllCities()
        tableView.reloadData()
    }
    
}

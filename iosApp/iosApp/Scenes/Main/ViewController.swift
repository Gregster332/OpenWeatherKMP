import UIKit
import MultiPlatformLibrary
import MultiPlatformLibraryMvvm

class ViewController: UIViewController {

    @IBOutlet private weak var tableView: UITableView!
    @IBOutlet private weak var textField: UITextField!
    private let refreshControl = UIRefreshControl()
    let ind = Indicator()
    
    private var viewModel: SimpleViewModel!
    private var cityArray: [RealmCityModel] = []
    private var locationManager = LocationManager.shared
    
    
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
        tableView.refreshControl = refreshControl
        refreshControl.addTarget(self, action: #selector(refreshWeather), for: .valueChanged)

    }
    
    private func createNeedToRefreshView(deleteViews: Bool) {
        let refreshView = UIView()
        refreshView.backgroundColor = UIColor(red: 49/255, green: 182/255, blue: 214/255, alpha: 1)
        refreshView.layer.cornerRadius = 15
        refreshView.tag = 100
        
        let textRefresh = UILabel()
        textRefresh.text = "Need to refresh one more time"
        textRefresh.font = UIFont.systemFont(ofSize: 15)
        textRefresh.numberOfLines = 0
        textRefresh.textAlignment = .center
        textRefresh.textColor = .white
        refreshView.addSubview(textRefresh)
        
        
        if deleteViews == false {
            self.navigationController?.view.addSubview(refreshView)
            refreshView.translatesAutoresizingMaskIntoConstraints = false
            refreshView.centerXAnchor.constraint(equalTo: view.centerXAnchor).isActive = true
            refreshView.centerYAnchor.constraint(equalTo: view.centerYAnchor).isActive = true
            refreshView.widthAnchor.constraint(equalToConstant: 200).isActive = true
            refreshView.heightAnchor.constraint(equalToConstant: 100).isActive = true
            textRefresh.translatesAutoresizingMaskIntoConstraints = false
            textRefresh.centerXAnchor.constraint(equalTo: view.centerXAnchor).isActive = true
            textRefresh.centerYAnchor.constraint(equalTo: view.centerYAnchor).isActive = true
            textRefresh.widthAnchor.constraint(equalToConstant: 150).isActive = true
            textRefresh.heightAnchor.constraint(equalToConstant: 60).isActive = true
        } else {
            if let viewWithTag = self.navigationController?.view.viewWithTag(100) {
                //textRefresh.removeFromSuperview()
                viewWithTag.removeFromSuperview()
            }
        }
        
    }
    
    @IBAction func onCounterButtonPressed() {
        viewModel.addCityToDB(name: textField.text!)
         cityArray = viewModel.fetchAllCities()
        if (viewModel.error.value == true) {
            showMiracle()
        }
        view.endEditing(true)
        textField.text = ""
        tableView.reloadData()
    }
    
    @IBAction func deleteAllCities() {
        viewModel.deleteAllCities()
        cityArray = viewModel.fetchAllCities()
        tableView.reloadData()
    }
    
    @objc private func refreshWeather() {
        if !cityArray.isEmpty {
            viewModel.refreshCities()
            cityArray = viewModel.fetchAllCities()
            createNeedToRefreshView(deleteViews: false)
           //ind.showIndicator()
        } else {
            createNeedToRefreshView(deleteViews: true)
            //ind.hideIndicator()
            cityArray = viewModel.fetchAllCities()
        }
        refreshControl.endRefreshing()
        tableView.reloadData()
    }
    
    @objc func showMiracle() {
        let slideVC = OverlayView()
        slideVC.viewModel = viewModel
        slideVC.modalPresentationStyle = .custom
        slideVC.transitioningDelegate = self
        self.present(slideVC, animated: true, completion: nil)
    }
    
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

extension ViewController: UIViewControllerTransitioningDelegate {
    func presentationController(forPresented presented: UIViewController, presenting: UIViewController?, source: UIViewController) -> UIPresentationController? {
        PresentationController(presentedViewController: presented, presenting: presenting)
    }
}

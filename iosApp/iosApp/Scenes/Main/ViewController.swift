import UIKit
import MultiPlatformLibrary
import MultiPlatformLibraryMvvm

class ViewController: UIViewController {

    @IBOutlet private weak var tableView: UITableView!
    @IBOutlet private weak var textField: UITextField!
    @IBOutlet private weak var currentCityView: UIView!
    
    private let refreshControl = UIRefreshControl()
    let ind = Indicator()
    
    var viewModel: SimpleViewModel!
    var locationManager: LocationManager!
    
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        locationManager = LocationManager()
        viewModel = SimpleViewModel(eventsDispatcher: .init())
        viewModel.fetchAllCities()
        tableViewRegister()
        textField.addTarget(self, action: #selector(textFieldDidChanged), for: .editingDidEndOnExit)
        
    }
    
    private func tableViewRegister() {
        tableView.delegate = self
        tableView.dataSource = self
        tableView.register(UINib(nibName: "CityCell", bundle: nil), forCellReuseIdentifier: "CityCell")
        tableView.refreshControl = refreshControl
        refreshControl.addTarget(self, action: #selector(refreshWeather), for: .valueChanged)

    }
    
    @objc private func textFieldDidChanged() {
        if textField.text!.isEmpty {
            self.view.endEditing(true)
        } else {
            onCounterButtonPressed()
        }
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
        guard let text = textField.text else { return }
        ind.showIndicator()
        viewModel.checkAndAddNewCity(name: text) { result in
            if result == LoadingState.success {
                self.view.endEditing(true)
                self.textField.text = ""
                self.viewModel.fetchAllCities()
                self.tableView.reloadData()
                self.ind.hideIndicator()
            } else {
                self.textField.text = ""
                self.ind.hideIndicator()
                self.showMiracle()
            }
        }
    }
    
    @IBAction func deleteAllCities() {
        viewModel.realm.deleteAllCities()
        viewModel.fetchAllCities()
        tableView.reloadData()
    }
    
    @objc private func refreshWeather() {
        ind.showIndicator()
        currentCityView.setNeedsDisplay()
        if viewModel.cities.count != 0 {
            viewModel.refresh {
                self.viewModel.fetchAllCities()
                self.update()
                DispatchQueue.main.async {
                    self.ind.hideIndicator()
                }
            }
        } else {
            update()
            ind.hideIndicator()
        }
        print("Here")
    }
    
    @objc func showMiracle() {
        let slideVC = OverlayView()
        slideVC.viewModel = viewModel
        slideVC.modalPresentationStyle = .custom
        slideVC.transitioningDelegate = self
        self.present(slideVC, animated: true, completion: nil)
    }
    
}


extension ViewController: UITableViewDelegate, UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        viewModel.cities.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cities = viewModel.cities as! [RealmCityModel]
        let cell = tableView.dequeueReusableCell(withIdentifier: "CityCell", for: indexPath) as! CityCell
        cell.setCellContent(data: cities[indexPath.row])
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
        let sb = UIStoryboard(name: "ViewController", bundle: nil)
        let controller = sb.instantiateViewController(withIdentifier: "DetailViewController") as! DetailViewController
        controller.welcome = viewModel.cities[indexPath.row] as! RealmCityModel
        self.navigationController?.pushViewController(controller, animated: true)
    }
    
    func tableView(_ tableView: UITableView, canEditRowAt indexPath: IndexPath) -> Bool {
        return true
    }
    
    func tableView(_ tableView: UITableView, commit editingStyle: UITableViewCell.EditingStyle, forRowAt indexPath: IndexPath) {
        viewModel.deleteCity(name: (viewModel.cities[indexPath.row] as AnyObject).name)
        viewModel.fetchAllCities()
        tableView.reloadData()
    }
    
}

extension ViewController: UIViewControllerTransitioningDelegate {
    func presentationController(forPresented presented: UIViewController, presenting: UIViewController?, source: UIViewController) -> UIPresentationController? {
        PresentationController(presentedViewController: presented, presenting: presenting)
    }
}


extension ViewController: SimpleViewModelEventsListener {
    func error(message: String) {
        print("error")
    }
    
    func isLoading(isLoading: Bool) {
        if isLoading {
            tableView.refreshControl?.beginRefreshing()
        } else {
            tableView.refreshControl?.endRefreshing()
        }
    }
    
    func update() {
        DispatchQueue.main.async {
            self.tableView.reloadData()
            self.tableView.refreshControl?.endRefreshing()
        }
    }
    
    
}
